package org.example.zoomlion.MaintenanceUI;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.zoomlion.TableViewFactory.AbstractMaintenanceTable;
import org.example.zoomlion.TableViewFactory.LubricationMaintenanceTable;
import org.example.zoomlion.TableViewFactory.MaintenanceTable;
import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.Utils.ListUtils;
import org.example.zoomlion.Utils.MaintenanceCalculator;
import org.example.zoomlion.models.Technic;

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

    List<Integer> mergedMaintenanceList;

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
        List<Integer> maintenanceList = getMaintenanceList();
        List<Integer> lubricationList = getLubricationList();
        mergedMaintenanceList = ListUtils.mergeAndSort(maintenanceList, lubricationList);

        if (!mergedMaintenanceList.isEmpty()) {
            Label toLabel = new Label(label);
            toLabel.getStyleClass().add("to-label");
            container.getChildren().add(toLabel);

            toggleButtonContainer = new HBox();
            toggleButtonContainer.setAlignment(Pos.CENTER_LEFT);

            toggleGroup = new ToggleGroup();
            createToggleButtons(toggleButtonContainer, toggleGroup, mergedMaintenanceList);

            TextField valueInput = new TextField();
            valueInput.setPrefWidth(100);
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
                    showAlert("Ошибка", "Введите значение перед расчетом!");
                    return;
                }

                int maintenanceValue = Integer.parseInt(inputValue);
                calculateNextMaintenanceAction(maintenanceValue);
            });

            toggleButtonContainer.getChildren().add(valueInput);
            toggleButtonContainer.getChildren().add(calculateNextMaintenance);

            container.getChildren().add(toggleButtonContainer);
        }

        if (!maintenanceList.isEmpty()) {
            MaintenanceTable<T> maintenanceTable = new MaintenanceTable.Builder<T>()
                    .setValueColumnLabel(valueColumnLabel)
                    .setValueColumnProperty(valueColumnProperty)
                    .build();

            addToggleListener(maintenanceTable, true);
            container.getChildren().add(maintenanceTable.getTableContainer());
        }

        if (!lubricationList.isEmpty()) {
            LubricationMaintenanceTable<L> lubricationTable = new LubricationMaintenanceTable.Builder<L>()
                    .setValueColumnLabel(lubricationValueColumnLabel)
                    .setValueColumnProperty(valueColumnProperty)
                    .build();

            addToggleListener(lubricationTable, false);
            container.getChildren().add(lubricationTable.getTableContainer());
        }
    }

    protected abstract List<Integer> getMaintenanceList();
    protected abstract List<Integer> getLubricationList();

    private void addToggleListener(AbstractMaintenanceTable<?> table, boolean isMaintenance) {
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                int value = Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""));
                if (isMaintenance) {
                    ((AbstractMaintenanceTable<T>) table).updateTable(() -> fetchMaintenanceData(value));
                } else {
                    ((AbstractMaintenanceTable<L>) table).updateTable(() -> fetchLubricationData(value));
                }
            } else {
                table.hideTable();
            }
        });
    }

    protected abstract List<T> fetchMaintenanceData(int value);
    protected abstract List<L> fetchLubricationData(int value);

    private void createToggleButtons(HBox toggleButtonContainer, ToggleGroup toggleGroup,
                                     List<Integer> filterParamsTOList) {
        for (Integer mileage : filterParamsTOList) {
            ToggleButton button = new ToggleButton(Constants.TO_LABEL + mileage);
            button.setToggleGroup(toggleGroup);
            button.getStyleClass().add("toggle-button");
            toggleButtonContainer.getChildren().add(button);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void calculateNextMaintenanceAction(int maintenanceValue) {
        int nextMaintenance = MaintenanceCalculator.getNextMaintenance(maintenanceValue, mergedMaintenanceList);

        for (Toggle toggle : toggleGroup.getToggles()) {
            ToggleButton button = (ToggleButton) toggle;
            if (button.getText().equals(Constants.TO_LABEL + nextMaintenance)) {
                button.setSelected(true);
                break;
            }
        }
    }

    public VBox getUI() {
        return container;
    }
}
