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
import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.models.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class CraneUIFactory implements TechnicUIFactory {
    private VBox mainContainer;


    private HBox mileageToggleButtonContainer;
    private ToggleGroup mileageToggleGroup;

    private VBox mileageTableContainer;
    private TableView<MileageMaintenance> mileageMaintenanceTableView;
    private ObservableList<MileageMaintenance> mileageMaintenanceObservableList;

    private VBox mileageLubricationTableContainer;
    private TableView<MileageLubrication> mileageLubricationTableView;
    private ObservableList<MileageLubrication> mileageLubricationObservableList;


    private HBox workHoursToggleButtonContainer;
    private ToggleGroup workHoursToggleGroup;

    private VBox workHoursTableContainer;
    private TableView<WorkHoursMaintenance> workHoursMaintenanceTableView;
    private ObservableList<WorkHoursMaintenance> workHoursMaintenanceObservableList;

    private VBox workHoursLubricationTableContainer;
    private TableView<WorkHoursLubrication> workHoursLubricationTableView;
    private ObservableList<WorkHoursLubrication> workHoursLubricationObservableList;


    private Technic technic;


    @Override
    public Node createUI(Technic technic) {
        this.technic = technic;

        mainContainer = new VBox();

        createMileageMaintenanceUI();
        createWorkHoursMaintenanceUI();

        return mainContainer;
    }

    private void createMileageMaintenanceUI() {
        List<Integer> mileageList = MaintenanceDAO.getMileageListByTechnicId(technic.getId());
        Collections.sort(mileageList);

        List<Integer> mileageLubricationList = MaintenanceDAO.getMileageLubricationListByTechnicId(technic.getId());
        Collections.sort(mileageLubricationList);

        Set<Integer> mileageSet = new HashSet<>(mileageList);
        mileageSet.addAll(mileageLubricationList);
        List<Integer> mergedMileageList = new ArrayList<>(mileageSet);
        Collections.sort(mergedMileageList);

        if (!mergedMileageList.isEmpty()) {
            mainContainer.getChildren().add(new Label(Constants.MILEAGE_TO_LABEL));

            mileageToggleButtonContainer = new HBox();
            mileageToggleGroup = new ToggleGroup();
            createToggleButtons(mileageToggleButtonContainer, mileageToggleGroup, mergedMileageList);

            mainContainer.getChildren().add(mileageToggleButtonContainer);
        }

        if (!mileageList.isEmpty()) {
            mileageTableContainer = new VBox();
            mileageTableContainer.setVisible(false);

            mileageMaintenanceTableView = new TableView<>();
            mileageMaintenanceObservableList = FXCollections.observableArrayList();
            mileageMaintenanceTableView.setItems(mileageMaintenanceObservableList);

            TableColumn<MileageMaintenance, String> maintenanceObjectColumn = new TableColumn<>(Constants.MAINTENANCE_OBJECT_LABEL);
            maintenanceObjectColumn.setCellValueFactory(new PropertyValueFactory<>("maintenanceObject"));

            TableColumn<MileageMaintenance, String> workContentColumn = new TableColumn<>(Constants.WORK_CONTENTS_LABEL);
            workContentColumn.setCellValueFactory(new PropertyValueFactory<>("workContent"));

            TableColumn<MileageMaintenance, Integer> mileageColumn = new TableColumn<>(Constants.MILEAGE_LABEL);
            mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));

            TableColumn<MileageMaintenance, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
            additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));

            mileageMaintenanceTableView.getColumns().addAll(maintenanceObjectColumn, workContentColumn, mileageColumn, additionalInfoColumn);

            mileageTableContainer.getChildren().add(mileageMaintenanceTableView);

            mileageToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    updateTable(
                            mileageTableContainer, mileageMaintenanceObservableList,
                            mileageMaintenanceTableView,
                            () -> MaintenanceDAO.getMileageMaintenanceByTechnicId(technic.getId(),
                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
                            )
                    );
                } else {
                    mileageTableContainer.setVisible(false);
                }
            });

            mainContainer.getChildren().add(mileageTableContainer);
        }

        if (!mileageLubricationList.isEmpty()) {
            mileageLubricationTableContainer = new VBox();
            mileageLubricationTableContainer.setVisible(false);

            mileageLubricationTableView = new TableView<>();
            mileageLubricationObservableList = FXCollections.observableArrayList();
            mileageLubricationTableView.setItems(mileageLubricationObservableList);

            TableColumn<MileageLubrication, String> lubricationPointColumn = new TableColumn<>(Constants.LUBRICATION_POINT_LABEL);
            lubricationPointColumn.setCellValueFactory(new PropertyValueFactory<>("lubricationPoint"));

            TableColumn<MileageLubrication, String> lubricationMethodColumn = new TableColumn<>(Constants.LUBRICATION_METHOD_LABEL);
            lubricationMethodColumn.setCellValueFactory(new PropertyValueFactory<>("lubricationMethod"));

            TableColumn<MileageLubrication, Integer> mileageColumn = new TableColumn<>(Constants.LUBRICATION_MILEAGE_LABEL);
            mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));

            TableColumn<MileageLubrication, String> lubricantColumn = new TableColumn<>(Constants.LUBRICANT_LABEL);
            lubricantColumn.setCellValueFactory(new PropertyValueFactory<>("lubricant"));

            TableColumn<MileageLubrication, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
            additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));

            mileageLubricationTableView.getColumns().addAll(lubricationPointColumn, lubricationMethodColumn, mileageColumn, lubricantColumn, additionalInfoColumn);

            mileageLubricationTableContainer.getChildren().add(mileageLubricationTableView);

            mileageToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    updateTable(
                            mileageLubricationTableContainer, mileageLubricationObservableList,
                            mileageLubricationTableView,
                            () -> MaintenanceDAO.getMileageLubricationByTechnicId(technic.getId(),
                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
                            )
                    );
                } else {
                    mileageLubricationTableContainer.setVisible(false);
                }
            });

            mainContainer.getChildren().add(mileageLubricationTableContainer);
        }
    }

    private void createWorkHoursMaintenanceUI() {
        List<Integer> workHoursList = MaintenanceDAO.getWorkHoursListByTechnicId(technic.getId());
        Collections.sort(workHoursList);

        List<Integer> workHoursLubricationList = MaintenanceDAO.getWorkHoursLubricationListByTechnicId(technic.getId());
        Collections.sort(workHoursLubricationList);

        Set<Integer> workHoursSet = new HashSet<>(workHoursList);
        workHoursList.addAll(workHoursLubricationList);
        List<Integer> mergedWorkHoursList = new ArrayList<>(workHoursSet);
        Collections.sort(mergedWorkHoursList);

        if (!mergedWorkHoursList.isEmpty()) {
            mainContainer.getChildren().add(new Label(Constants.WORK_HOURS_TO_LABEL));

            workHoursToggleButtonContainer = new HBox();
            workHoursToggleGroup = new ToggleGroup();
            createToggleButtons(workHoursToggleButtonContainer, workHoursToggleGroup, mergedWorkHoursList);

            mainContainer.getChildren().add(workHoursToggleButtonContainer);
        }

        if (!workHoursList.isEmpty()) {
            workHoursTableContainer = new VBox();
            workHoursTableContainer.setVisible(false);

            workHoursMaintenanceTableView = new TableView<>();
            workHoursMaintenanceObservableList = FXCollections.observableArrayList();
            workHoursMaintenanceTableView.setItems(workHoursMaintenanceObservableList);

            TableColumn<WorkHoursMaintenance, String> maintenanceObjectColumn = new TableColumn<>(Constants.MAINTENANCE_OBJECT_LABEL);
            maintenanceObjectColumn.setCellValueFactory(new PropertyValueFactory<>("maintenanceObject"));

            TableColumn<WorkHoursMaintenance, String> workContentColumn = new TableColumn<>(Constants.WORK_CONTENTS_LABEL);
            workContentColumn.setCellValueFactory(new PropertyValueFactory<>("workContent"));

            TableColumn<WorkHoursMaintenance, Integer> workHoursColumn = new TableColumn<>(Constants.WORK_HOURS_LABEL);
            workHoursColumn.setCellValueFactory(new PropertyValueFactory<>("workHours"));

            TableColumn<WorkHoursMaintenance, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
            additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));

            workHoursMaintenanceTableView.getColumns().addAll(maintenanceObjectColumn, workContentColumn, workHoursColumn, additionalInfoColumn);

            workHoursTableContainer.getChildren().add(workHoursMaintenanceTableView);

            workHoursToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    updateTable(
                            workHoursTableContainer, workHoursMaintenanceObservableList,
                            workHoursMaintenanceTableView,
                            () -> MaintenanceDAO.getWorkHoursMaintenanceByTechnicId(technic.getId(),
                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
                            )
                    );
                } else {
                    workHoursTableContainer.setVisible(false);
                }
            });

            mainContainer.getChildren().add(workHoursTableContainer);
        }

        if (!workHoursLubricationList.isEmpty()) {
            workHoursLubricationTableContainer = new VBox();
            workHoursLubricationTableContainer.setVisible(false);

            workHoursLubricationTableView = new TableView<>();
            workHoursLubricationObservableList = FXCollections.observableArrayList();
            workHoursLubricationTableView.setItems(workHoursLubricationObservableList);

            TableColumn<WorkHoursLubrication, String> lubricationPointColumn = new TableColumn<>(Constants.LUBRICATION_POINT_LABEL);
            lubricationPointColumn.setCellValueFactory(new PropertyValueFactory<>("lubricationPoint"));

            TableColumn<WorkHoursLubrication, String> lubricationMethodColumn = new TableColumn<>(Constants.LUBRICATION_POINT_LABEL);
            lubricationMethodColumn.setCellValueFactory(new PropertyValueFactory<>("lubricationMethod"));

            TableColumn<WorkHoursLubrication, Integer> workHoursColumn = new TableColumn<>(Constants.LUBRICATION_WORK_HOURS_LABEL);
            workHoursColumn.setCellValueFactory(new PropertyValueFactory<>("workHours"));

            TableColumn<WorkHoursLubrication, String> lubricantColumn = new TableColumn<>(Constants.LUBRICANT_LABEL);
            lubricantColumn.setCellValueFactory(new PropertyValueFactory<>("lubricant"));

            TableColumn<WorkHoursLubrication, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
            additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));

            workHoursLubricationTableView.getColumns().addAll(lubricationPointColumn, lubricationMethodColumn, workHoursColumn, lubricantColumn, additionalInfoColumn);

            workHoursLubricationTableContainer.getChildren().add(workHoursLubricationTableView);

            workHoursToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    updateTable(
                            workHoursLubricationTableContainer, workHoursLubricationObservableList,
                            workHoursLubricationTableView,
                            () -> MaintenanceDAO.getWorkHoursLubricationByTechnicId(technic.getId(),
                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
                            )
                    );
                } else {
                    mileageLubricationTableContainer.setVisible(false);
                }
            });

            mainContainer.getChildren().add(mileageLubricationTableContainer);
        }
    }

    /**
     * Создание кнопок ТО (ТО-100, ТО-250, ТО-500, ТО-750) динамически
     */
    private void createToggleButtons(HBox toggleButtonContainer, ToggleGroup toggleGroup, List<Integer> filterParamsTOList) {
        for (Integer mileage : filterParamsTOList) {
            ToggleButton button = new ToggleButton(Constants.TO_LABEL + mileage);
            button.setToggleGroup(toggleGroup);
            button.getStyleClass().add("toggle-button");
            toggleButtonContainer.getChildren().add(button);
        }
    }

    /**
     * Обновление таблицы при выборе ТО
     */
    private <T> void updateTable(VBox tableContainer, ObservableList<T> observableList, TableView<T> tableView,
                                 Supplier<List<T>> dataLoader) {
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

        tableView.getColumns().removeIf(col -> col.getText().equals(Constants.ADDITIONAL_INFO_LABEL));

        if (hasAdditionalInfo) {
            TableColumn<T, String> additionalInfoColumn = getAdditionalInfoColumn();
            tableView.getColumns().add(additionalInfoColumn);
        }
    }

    private static <T> TableColumn<T, String> getAdditionalInfoColumn() {
        TableColumn<T, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
        additionalInfoColumn.setCellValueFactory(cellData -> {
            try {
                Method method = cellData.getValue().getClass().getMethod("additionalInfoProperty");
                return (ObservableValue<String>) method.invoke(cellData.getValue());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return new SimpleStringProperty("");
            }
        });
        return additionalInfoColumn;
    }
}

// 335 code lines
// 2092 all