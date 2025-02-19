package org.example.zoomlion.UIFactory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.zoomlion.DB.MaintenanceDAO;
import org.example.zoomlion.models.MileageMaintenance;
import org.example.zoomlion.models.Technic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class CraneUIFactory implements TechnicUIFactory {
    private HBox mileageToggleButtonContainer;
    private ToggleGroup mileageToggleGroup;
    private VBox mileageTableContainer;
    private TableView<MileageMaintenance> mileageMaintenanceTableView;
    private ObservableList<MileageMaintenance> mileageMaintenanceObservableList;

    @Override
    public Node createUI(Technic technic) {
        VBox vbox = new VBox(10);

        List<Integer> mileageList = MaintenanceDAO.getMileageListByTechnicId(technic.getId());
        Collections.sort(mileageList);

        List<Integer> mileageLubricationList = MaintenanceDAO.getMileageLubricationListByTechnicId(technic.getId());
        Collections.sort(mileageLubricationList);

        Set<Integer> mileageSet = new HashSet<>(mileageList);
        mileageSet.addAll(mileageLubricationList);
        List<Integer> mergedMileageList = new ArrayList<>(mileageSet);
        Collections.sort(mergedMileageList);

        if (!mergedMileageList.isEmpty()) {
            vbox.getChildren().add(new Label("ТО по пробегу:"));

            mileageToggleButtonContainer = new HBox();
            mileageToggleGroup = new ToggleGroup();
            createToggleButtons(mileageToggleButtonContainer, mileageToggleGroup, mergedMileageList);
        }

        if (!mileageList.isEmpty()) {
            mileageTableContainer = new VBox();
            mileageTableContainer.setVisible(false);

            mileageMaintenanceTableView = new TableView<>();
            mileageMaintenanceObservableList = FXCollections.observableArrayList();
            mileageMaintenanceTableView.setItems(mileageMaintenanceObservableList);

            TableColumn<MileageMaintenance, String> maintenanceObjectColumn = new TableColumn<>("Объекты обслуживания");
            maintenanceObjectColumn.setCellValueFactory(new PropertyValueFactory<>("maintenanceObject"));

            TableColumn<MileageMaintenance, String> workContentColumn = new TableColumn<>("Содержание работ");
            workContentColumn.setCellValueFactory(new PropertyValueFactory<>("workContent"));

            TableColumn<MileageMaintenance, Integer> mileageColumn = new TableColumn<>("Пробег, км");
            mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));

            TableColumn<MileageMaintenance, String> additionalInfoColumn = new TableColumn<>("Доп. информация");
            additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));

            mileageMaintenanceTableView.getColumns().addAll(maintenanceObjectColumn, workContentColumn, mileageColumn, additionalInfoColumn);

            mileageTableContainer.getChildren().add(mileageMaintenanceTableView);

            mileageToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    updateTable(
                            mileageTableContainer, mileageMaintenanceObservableList,
                            mileageMaintenanceTableView,
                            () -> MaintenanceDAO.getMileageMaintenanceByTechnicId(technic.getId(),
                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace("ТО-", ""))
                            )
                    );
                } else {
                    mileageTableContainer.setVisible(false);
                }
            });

            vbox.getChildren().addAll(mileageToggleButtonContainer, mileageTableContainer);
        }

        return vbox;
    }

    /**
     * Создание кнопок ТО (ТО100, ТО250, ТО500, ТО750) динамически
     */
    private void createToggleButtons(HBox toggleButtonContainer, ToggleGroup toggleGroup, List<Integer> mileageList) {
        for (Integer mileage : mileageList) {
            ToggleButton button = new ToggleButton("ТО-" + mileage);
            button.setToggleGroup(toggleGroup);
            button.getStyleClass().add("toggle-button");
            toggleButtonContainer.getChildren().add(button);
        }
    }

    /**
     * Обновление таблицы при выборе ТО
     */
    private <T> void updateTable(VBox tableContainer, ObservableList<T> observableList,
                                 TableView<T> tableView, Supplier<List<T>> dataLoader) {
        tableContainer.setVisible(true);
        observableList.clear();
        observableList.addAll(dataLoader.get());

        setAdditionalInfoColumn(observableList, tableView);
    }

    private static <T> void setAdditionalInfoColumn(ObservableList<T> observableList, TableView<T> tableView) {
        boolean hasAdditionalInfo = observableList.stream().anyMatch(item -> {
            try {
                Method method = item.getClass().getMethod("getAdditionalInfo");
                Object value = method.invoke(item);
                return value != null && !value.toString().isEmpty();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return false;
            }
        });

        tableView.getColumns().removeIf(col -> col.getText().equals("Доп. информация"));

        if (hasAdditionalInfo) {
            TableColumn<T, String> additionalInfoColumn = new TableColumn<>("Доп. информация");
            additionalInfoColumn.setCellValueFactory(cellData -> {
                try {
                    Method method = cellData.getValue().getClass().getMethod("additionalInfoProperty");
                    return (ObservableValue<String>) method.invoke(cellData.getValue());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    return new SimpleStringProperty("");
                }
            });
            tableView.getColumns().add(additionalInfoColumn);
        }
    }
}