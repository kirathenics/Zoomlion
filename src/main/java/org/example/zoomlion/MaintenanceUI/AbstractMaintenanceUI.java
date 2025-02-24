package org.example.zoomlion.MaintenanceUI;

import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.zoomlion.TableViewFactory.AbstractMaintenanceTable;
import org.example.zoomlion.TableViewFactory.LubricationMaintenanceTable;
import org.example.zoomlion.TableViewFactory.MaintenanceTable;
import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.Utils.ListUtils;
import org.example.zoomlion.models.Technic;

import java.util.List;

public abstract class AbstractMaintenanceUI<T, L> {
    protected VBox container;
    protected HBox toggleButtonContainer;
    protected ToggleGroup toggleGroup;
    protected Technic technic;
    protected String label;
    protected String valueColumnLabel;
    protected String valueColumnProperty;

    public AbstractMaintenanceUI(Technic technic, String label, String valueColumnLabel, String valueColumnProperty) {
        this.technic = technic;
        this.label = label;
        this.valueColumnLabel = valueColumnLabel;
        this.valueColumnProperty = valueColumnProperty;
        this.container = new VBox();
        createUI();
    }

    private void createUI() {
        List<Integer> maintenanceList = getMaintenanceList();
        List<Integer> lubricationList = getLubricationList();
        List<Integer> mergedList = ListUtils.mergeAndSort(maintenanceList, lubricationList);

        if (!mergedList.isEmpty()) {
            container.getChildren().add(new Label(label));

            toggleButtonContainer = new HBox();
            toggleGroup = new ToggleGroup();
            createToggleButtons(toggleButtonContainer, toggleGroup, mergedList);

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
                    .setValueColumnLabel(valueColumnLabel)
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

    public VBox getUI() {
        return container;
    }
}
