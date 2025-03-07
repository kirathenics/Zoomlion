package org.example.zoomlion.MaintenanceUI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.zoomlion.TableViewFactory.AbstractMaintenanceTable;
import org.example.zoomlion.TableViewFactory.LubricationMaintenanceTable;
import org.example.zoomlion.TableViewFactory.MaintenanceTable;
import org.example.zoomlion.utils.Constants;
import org.example.zoomlion.utils.ListUtils;
import org.example.zoomlion.utils.MaintenanceCalculator;
import org.example.zoomlion.utils.UserDialogs;
import org.example.zoomlion.models.MaintenanceValue;
import org.example.zoomlion.models.Technic;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMaintenanceUI<T, L> {
    protected VBox container;
    protected HBox toggleButtonContainer;
    protected ToggleGroup toggleGroup;
    protected Technic technic;
    protected String label;
    protected String valueColumnLabel;
    protected String lubricationValueColumnLabel;
    protected String valueColumnProperty;

    private List<MaintenanceValue> mergedMaintenanceList;

    private boolean hasNonPeriodicMaintenance = false;

    public AbstractMaintenanceUI(Technic technic,
                                 String label,
                                 String valueColumnLabel,
                                 String lubricationValueColumnLabel,
                                 String valueColumnProperty) {
        this.technic = technic;
        this.label = label;
        this.valueColumnLabel = valueColumnLabel;
        this.valueColumnProperty = valueColumnProperty;
        this.lubricationValueColumnLabel = lubricationValueColumnLabel;
        this.container = new VBox();
        createUI();
    }

    private void createUI() {
        List<MaintenanceValue> maintenanceList = getMaintenanceList();
        List<MaintenanceValue> lubricationList = getLubricationList();
        mergedMaintenanceList = ListUtils.mergeAndSort(maintenanceList, lubricationList);

        if (!mergedMaintenanceList.isEmpty()) {
            Label toLabel = new Label(label);
            toLabel.getStyleClass().add("to-label");
            container.getChildren().add(toLabel);

            toggleButtonContainer = new HBox();

            toggleGroup = new ToggleGroup();
            createToggleButtons(toggleButtonContainer, toggleGroup, mergedMaintenanceList);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            TextField valueInput = new TextField();
            HBox.setMargin(valueInput, new Insets(10));
            valueInput.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    valueInput.setText(newValue.replaceAll("\\D", ""));
                }
            });

            Button calculateNextMaintenance = new Button();
            calculateNextMaintenance.setText(Constants.CALCULATE_NEXT_MAINTENANCE_LABEL);
            calculateNextMaintenance.getStyleClass().add("calculate-button");
            calculateNextMaintenance.setOnAction(event -> {
                String inputValue = valueInput.getText();
                if (inputValue.isEmpty()) {
                    UserDialogs.showAlert(Constants.ERROR_LABEL, Constants.ENTER_VALUE_LABEL);
                    return;
                }

                int maintenanceValue = Integer.parseInt(inputValue);
                calculateNextMaintenanceAction(maintenanceValue);
            });

            toggleButtonContainer.getChildren().addAll(spacer, valueInput, calculateNextMaintenance);

            container.getChildren().add(toggleButtonContainer);

            if (hasNonPeriodicMaintenance) {
                createAsteriskLabel();
            }
        }

        if (!maintenanceList.isEmpty()) {
            MaintenanceTable<T> maintenanceTable = new MaintenanceTable.Builder<T>()
                    .setValueColumnLabel(valueColumnLabel)
                    .build();

            addToggleListener(maintenanceTable, true);
            container.getChildren().add(maintenanceTable.getTableContainer());
        }

        if (!lubricationList.isEmpty()) {
            LubricationMaintenanceTable<L> lubricationTable = new LubricationMaintenanceTable.Builder<L>()
                    .setValueColumnLabel(lubricationValueColumnLabel)
                    .build();

            addToggleListener(lubricationTable, false);
            container.getChildren().add(lubricationTable.getTableContainer());
        }
    }

    protected abstract List<MaintenanceValue> getMaintenanceList();
    protected abstract List<MaintenanceValue> getLubricationList();

    private void addToggleListener(AbstractMaintenanceTable<?> table, boolean isMaintenance) {
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                List<Toggle> toggles = new ArrayList<>(toggleGroup.getToggles());
                int index = toggles.indexOf(newToggle);

                MaintenanceValue value = mergedMaintenanceList.get(index);

                if (isMaintenance) {
                    ((AbstractMaintenanceTable<T>) table).updateTable(() -> fetchMaintenanceData(value.getValue(), value.isPeriodic()));
                } else {
                    ((AbstractMaintenanceTable<L>) table).updateTable(() -> fetchLubricationData(value.getValue(), value.isPeriodic()));
                }
            } else {
                table.hideTable();
            }
        });
    }

    protected abstract List<T> fetchMaintenanceData(int value, Boolean isPeriodic);
    protected abstract List<L> fetchLubricationData(int value, Boolean isPeriodic);

    private void createToggleButtons(HBox toggleButtonContainer, ToggleGroup toggleGroup,
                                     List<MaintenanceValue> maintenanceValues) {
        VBox toggleButtonsContainer = new VBox();
        HBox currentRow = new HBox();
        toggleButtonsContainer.getChildren().add(currentRow);

        int maxButtonsPerRow = 6;
        int count = 0;

        for (MaintenanceValue maintenanceValue : maintenanceValues) {
            if (count >= maxButtonsPerRow) {
                currentRow = new HBox();
                toggleButtonsContainer.getChildren().add(currentRow);
                count = 0;
            }

            String buttonName = Constants.TO_LABEL + maintenanceValue.getValue();
            if (maintenanceValue.isPeriodic() != null && !maintenanceValue.isPeriodic()) {
                buttonName += "*";
                hasNonPeriodicMaintenance = true;
            }

            ToggleButton button = new ToggleButton(buttonName);
            button.setToggleGroup(toggleGroup);
            button.getStyleClass().add("toggle-button");
            currentRow.getChildren().add(button);
            count++;
        }

        toggleButtonContainer.getChildren().add(toggleButtonsContainer);
    }

    private void calculateNextMaintenanceAction(int maintenanceValue) {
        List<Integer> periodicMaintenanceList = mergedMaintenanceList.stream()
                .filter(e -> e.isPeriodicProperty() != null && e.isPeriodicProperty().getValue().equals(Boolean.TRUE))
                .map(MaintenanceValue::getValue)
                .toList();

        int nextMaintenance = MaintenanceCalculator.getNextMaintenance(maintenanceValue, periodicMaintenanceList);

        for (Toggle toggle : toggleGroup.getToggles()) {
            ToggleButton button = (ToggleButton) toggle;
            if (button.getText().equals(Constants.TO_LABEL + nextMaintenance)) {
                button.setSelected(true);
                break;
            }
        }
    }

    private void createAsteriskLabel() {
        Label asteriskLabel = new Label(Constants.ASTERISK_LABEL);
        asteriskLabel.getStyleClass().add("asterisk-label");
        container.getChildren().add(asteriskLabel);
    }

    public VBox getUI() {
        return container;
    }
}
