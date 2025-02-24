package org.example.zoomlion.TechnicMaintenanceUIFactory;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.zoomlion.DB.MaintenanceDAO;
import org.example.zoomlion.TableViewFactory.MaintenanceTable;
import org.example.zoomlion.TableViewFactory.LubricationMaintenanceTable;
import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.Utils.ListUtils;
import org.example.zoomlion.models.*;

import java.util.*;


// 335 code lines
// 2092 all


public class CraneMaintenanceUIFactory implements TechnicMaintenanceUIFactory {
    private VBox mainContainer;

    private HBox mileageToggleButtonContainer;
    private ToggleGroup mileageToggleGroup;

    private HBox workHoursToggleButtonContainer;
    private ToggleGroup workHoursToggleGroup;

    private Technic technic;

    @Override
    public Node createUI(Technic technic) {
        this.technic = technic;

        mainContainer = new VBox();
        mainContainer.setPrefWidth(870);

        createMileageMaintenanceUI();
        createWorkHoursMaintenanceUI();

        return mainContainer;
    }

    private void createMileageMaintenanceUI() {
        List<Integer> mileageList = MaintenanceDAO.getMileageListByTechnicId(technic.getId());
        List<Integer> mileageLubricationList = MaintenanceDAO.getMileageLubricationListByTechnicId(technic.getId());
        List<Integer> mergedMileageList = ListUtils.mergeAndSort(mileageList, mileageLubricationList);

        if (!mergedMileageList.isEmpty()) {
            mainContainer.getChildren().add(new Label(Constants.MILEAGE_TO_LABEL));

            mileageToggleButtonContainer = new HBox();
            mileageToggleGroup = new ToggleGroup();
            createToggleButtons(mileageToggleButtonContainer, mileageToggleGroup, mergedMileageList);

            mainContainer.getChildren().add(mileageToggleButtonContainer);

//            TextField textField = new TextField();
//            mileageToggleButtonContainer.getChildren().add(textField);
        }

        if (!mileageList.isEmpty()) {
            MaintenanceTable<MileageMaintenance> mileageMaintenanceTable = new MaintenanceTable.TableBuilder<MileageMaintenance>()
                    .setValueColumnLabel(Constants.MILEAGE_LABEL)
                    .setValueColumnProperty("mileage")
                    .build();

            mileageToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    mileageMaintenanceTable.updateTable(
                            () -> MaintenanceDAO.getMileageMaintenanceByTechnicId(technic.getId(),
                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
                            )
                    );
                } else {
                    mileageMaintenanceTable.hideTable();
                }
            });

            mainContainer.getChildren().add(mileageMaintenanceTable.getTableContainer());
        }

        if (!mileageLubricationList.isEmpty()) {
            LubricationMaintenanceTable<MileageLubrication> mileageLubricationMaintenanceTable = new LubricationMaintenanceTable.TableBuilder<MileageLubrication>()
                    .setValueColumnLabel(Constants.MILEAGE_LABEL)
                    .setValueColumnProperty("mileage")
                    .build();

            mileageToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    mileageLubricationMaintenanceTable.updateTable(
                            () -> MaintenanceDAO.getMileageLubricationByTechnicId(technic.getId(),
                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
                            )
                    );
                } else {
                    mileageLubricationMaintenanceTable.hideTable();
                }
            });

            mainContainer.getChildren().add(mileageLubricationMaintenanceTable.getTableContainer());
        }
    }

    private void createWorkHoursMaintenanceUI() {
        List<Integer> workHoursList = MaintenanceDAO.getWorkHoursListByTechnicId(technic.getId());
        List<Integer> workHoursLubricationList = MaintenanceDAO.getWorkHoursLubricationListByTechnicId(technic.getId());
        List<Integer> mergedWorkHoursList = ListUtils.mergeAndSort(workHoursList, workHoursLubricationList);

        if (!mergedWorkHoursList.isEmpty()) {
            mainContainer.getChildren().add(new Label(Constants.WORK_HOURS_TO_LABEL));

            workHoursToggleButtonContainer = new HBox();
            workHoursToggleGroup = new ToggleGroup();
            createToggleButtons(workHoursToggleButtonContainer, workHoursToggleGroup, mergedWorkHoursList);

            mainContainer.getChildren().add(workHoursToggleButtonContainer);
        }

        if (!workHoursList.isEmpty()) {
            MaintenanceTable<WorkHoursMaintenance> workHoursMaintenanceTable = new MaintenanceTable.TableBuilder<WorkHoursMaintenance>()
                    .setValueColumnLabel(Constants.WORK_HOURS_LABEL)
                    .setValueColumnProperty("workHours")
                    .build();

            workHoursToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    workHoursMaintenanceTable.updateTable(
                            () -> MaintenanceDAO.getWorkHoursMaintenanceByTechnicId(technic.getId(),
                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
                            )
                    );
                } else {
                    workHoursMaintenanceTable.hideTable();
                }
            });

            mainContainer.getChildren().add(workHoursMaintenanceTable.getTableContainer());
        }

        if (!workHoursLubricationList.isEmpty()) {
            LubricationMaintenanceTable<WorkHoursLubrication> workHoursLubricationMaintenanceTable = new LubricationMaintenanceTable.TableBuilder<WorkHoursLubrication>()
                    .setValueColumnLabel(Constants.WORK_HOURS_LABEL)
                    .setValueColumnProperty("workHours")
                    .build();

            workHoursToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    workHoursLubricationMaintenanceTable.updateTable(
                            () -> MaintenanceDAO.getWorkHoursLubricationByTechnicId(technic.getId(),
                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
                            )
                    );
                } else {
                    workHoursLubricationMaintenanceTable.hideTable();
                }
            });

            mainContainer.getChildren().add(workHoursLubricationMaintenanceTable.getTableContainer());
        }

    }

    /**
     * Создание кнопок ТО (ТО-100, ТО-250, ТО-500, ТО-750) динамически
     */
    private void createToggleButtons(HBox toggleButtonContainer, ToggleGroup toggleGroup,
                                     List<Integer> filterParamsTOList) {
        for (Integer mileage : filterParamsTOList) {
            ToggleButton button = new ToggleButton(Constants.TO_LABEL + mileage);
            button.setToggleGroup(toggleGroup);
            button.getStyleClass().add("toggle-button");
            toggleButtonContainer.getChildren().add(button);
        }
    }
}



//public class CraneUIFactory implements TechnicUIFactory {
//
//
//

//
//

//
//    private void createMileageMaintenanceUI() {
//        List<Integer> mileageList = MaintenanceDAO.getMileageListByTechnicId(technic.getId());
//        Collections.sort(mileageList);
//
//        List<Integer> mileageLubricationList = MaintenanceDAO.getMileageLubricationListByTechnicId(technic.getId());
//        Collections.sort(mileageLubricationList);
//
//        Set<Integer> mileageSet = new HashSet<>(mileageList);
//        mileageSet.addAll(mileageLubricationList);
//        List<Integer> mergedMileageList = new ArrayList<>(mileageSet);
//        Collections.sort(mergedMileageList);
//
//        if (!mergedMileageList.isEmpty()) {
//            mainContainer.getChildren().add(new Label(Constants.MILEAGE_TO_LABEL));
//
//            mileageToggleButtonContainer = new HBox();
//            mileageToggleGroup = new ToggleGroup();
//            createToggleButtons(mileageToggleButtonContainer, mileageToggleGroup, mergedMileageList);
//
//            mainContainer.getChildren().add(mileageToggleButtonContainer);
//
////            TextField textField = new TextField();
////            mileageToggleButtonContainer.getChildren().add(textField);
//        }
//
//        if (!mileageList.isEmpty()) {
//            mileageMaintenanceTableContainer = new VBox();
//
//            mileageMaintenanceTableView = new TableView<>();
//            mileageMaintenanceObservableList = FXCollections.observableArrayList();
//            mileageMaintenanceTableView.setItems(mileageMaintenanceObservableList);
//
//            adjustTableSize(mileageMaintenanceTableView, mileageMaintenanceTableContainer);
//            mileageMaintenanceTableView.getStyleClass().add("table-view");
//
//            TableColumn<MileageMaintenance, String> maintenanceObjectColumn = new TableColumn<>(Constants.MAINTENANCE_OBJECT_LABEL);
//            maintenanceObjectColumn.setCellValueFactory(new PropertyValueFactory<>("maintenanceObject"));
//
//            TableColumn<MileageMaintenance, String> workContentColumn = new TableColumn<>(Constants.WORK_CONTENTS_LABEL);
//            workContentColumn.setCellValueFactory(new PropertyValueFactory<>("workContent"));
//            workContentColumn.setCellFactory(tc -> createWrappedCell());
//
//            TableColumn<MileageMaintenance, Integer> mileageColumn = new TableColumn<>(Constants.MILEAGE_LABEL);
//            mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));
//
//            TableColumn<MileageMaintenance, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
//            additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));
//
//            maintenanceObjectColumn.prefWidthProperty().bind(mileageMaintenanceTableView.widthProperty().multiply(0.3));
//            workContentColumn.prefWidthProperty().bind(mileageMaintenanceTableView.widthProperty().multiply(0.5));
//            mileageColumn.prefWidthProperty().bind(mileageMaintenanceTableView.widthProperty().multiply(0.2));
//
//            mileageMaintenanceTableView.getColumns().addAll(maintenanceObjectColumn, workContentColumn, mileageColumn, additionalInfoColumn);
//
//            mileageMaintenanceTableContainer.getChildren().add(mileageMaintenanceTableView);
//
//            mileageToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
//                if (newToggle != null) {
//                    updateTable(
//                            mileageMaintenanceObservableList,
//                            mileageMaintenanceTableView,
//                            () -> MaintenanceDAO.getMileageMaintenanceByTechnicId(technic.getId(),
//                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
//                            )
//                    );
//                } else {
//                    mileageMaintenanceTableContainer.setVisible(false);
//                    mileageMaintenanceTableContainer.setManaged(false);
//                    mileageMaintenanceObservableList.clear();
//                }
//            });
//
//            mainContainer.getChildren().add(mileageMaintenanceTableContainer);
//        }
//
//        if (!mileageLubricationList.isEmpty()) {
//            mileageLubricationTableContainer = new VBox();
//            mileageLubricationTableContainer.setVisible(false);
//
//            mileageLubricationTableView = new TableView<>();
//            mileageLubricationObservableList = FXCollections.observableArrayList();
//            mileageLubricationTableView.setItems(mileageLubricationObservableList);
//
//            adjustTableSize(mileageLubricationTableView, mileageLubricationTableContainer);
//            mileageLubricationTableView.getStyleClass().add("table-view");
//
//            TableColumn<MileageLubrication, String> lubricationPointColumn = new TableColumn<>(Constants.LUBRICATION_POINT_LABEL);
//            lubricationPointColumn.setCellValueFactory(new PropertyValueFactory<>("lubricationPoint"));
//
//            TableColumn<MileageLubrication, String> lubricationMethodColumn = new TableColumn<>(Constants.LUBRICATION_METHOD_LABEL);
//            lubricationMethodColumn.setCellValueFactory(new PropertyValueFactory<>("lubricationMethod"));
//
//            TableColumn<MileageLubrication, Integer> mileageColumn = new TableColumn<>(Constants.LUBRICATION_MILEAGE_LABEL);
//            mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));
//
//            TableColumn<MileageLubrication, String> lubricantColumn = new TableColumn<>(Constants.LUBRICANT_LABEL);
//            lubricantColumn.setCellValueFactory(new PropertyValueFactory<>("lubricant"));
//
//            TableColumn<MileageLubrication, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
//            additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));
//
//            mileageLubricationTableView.getColumns().addAll(lubricationPointColumn, lubricationMethodColumn, mileageColumn, lubricantColumn, additionalInfoColumn);
//
//            mileageLubricationTableContainer.getChildren().add(mileageLubricationTableView);
//
//            mileageToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
//                if (newToggle != null) {
//                    updateTable(
//                            mileageLubricationObservableList,
//                            mileageLubricationTableView,
//                            () -> MaintenanceDAO.getMileageLubricationByTechnicId(technic.getId(),
//                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
//                            )
//                    );
//                } else {
//                    mileageLubricationTableContainer.setVisible(false);
//                }
//            });
//
//            mainContainer.getChildren().add(mileageLubricationTableContainer);
//        }
//    }
//
//    private void createWorkHoursMaintenanceUI() {
//        List<Integer> workHoursList = MaintenanceDAO.getWorkHoursListByTechnicId(technic.getId());
//        Collections.sort(workHoursList);
//
//        List<Integer> workHoursLubricationList = MaintenanceDAO.getWorkHoursLubricationListByTechnicId(technic.getId());
//        Collections.sort(workHoursLubricationList);
//
//        Set<Integer> workHoursSet = new HashSet<>(workHoursList);
//        workHoursList.addAll(workHoursLubricationList);
//        List<Integer> mergedWorkHoursList = new ArrayList<>(workHoursSet);
//        Collections.sort(mergedWorkHoursList);
//
//        if (!mergedWorkHoursList.isEmpty()) {
//            mainContainer.getChildren().add(new Label(Constants.WORK_HOURS_TO_LABEL));
//
//            workHoursToggleButtonContainer = new HBox();
//            workHoursToggleGroup = new ToggleGroup();
//            createToggleButtons(workHoursToggleButtonContainer, workHoursToggleGroup, mergedWorkHoursList);
//
//            mainContainer.getChildren().add(workHoursToggleButtonContainer);
//        }
//
//        if (!workHoursList.isEmpty()) {
//            workHoursMaintenanceTableContainer = new VBox();
////            workHoursTableContainer.setVisible(false);
//
//            workHoursMaintenanceTableView = new TableView<>();
//            workHoursMaintenanceObservableList = FXCollections.observableArrayList();
//            workHoursMaintenanceTableView.setItems(workHoursMaintenanceObservableList);
//
//            adjustTableSize(workHoursMaintenanceTableView, workHoursMaintenanceTableContainer);
//            workHoursMaintenanceTableView.getStyleClass().add("table-view");
//
//            TableColumn<WorkHoursMaintenance, String> maintenanceObjectColumn = new TableColumn<>(Constants.MAINTENANCE_OBJECT_LABEL);
//            maintenanceObjectColumn.setCellValueFactory(new PropertyValueFactory<>("maintenanceObject"));
//
//            TableColumn<WorkHoursMaintenance, String> workContentColumn = new TableColumn<>(Constants.WORK_CONTENTS_LABEL);
//            workContentColumn.setCellValueFactory(new PropertyValueFactory<>("workContent"));
//
//            TableColumn<WorkHoursMaintenance, Integer> workHoursColumn = new TableColumn<>(Constants.WORK_HOURS_LABEL);
//            workHoursColumn.setCellValueFactory(new PropertyValueFactory<>("workHours"));
//
//            TableColumn<WorkHoursMaintenance, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
//            additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));
//
//            workHoursMaintenanceTableView.getColumns().addAll(maintenanceObjectColumn, workContentColumn, workHoursColumn, additionalInfoColumn);
//
//            workHoursMaintenanceTableContainer.getChildren().add(workHoursMaintenanceTableView);
//
//            workHoursToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
//                if (newToggle != null) {
//                    updateTable(
//                            workHoursMaintenanceObservableList,
//                            workHoursMaintenanceTableView,
//                            () -> MaintenanceDAO.getWorkHoursMaintenanceByTechnicId(technic.getId(),
//                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
//                            )
//                    );
//                } else {
//                    workHoursMaintenanceTableContainer.setVisible(false);
//                    workHoursMaintenanceTableContainer.setManaged(false);
//                    workHoursMaintenanceObservableList.clear();
//                }
//            });
//
//            mainContainer.getChildren().add(workHoursMaintenanceTableContainer);
//        }
//
//        if (!workHoursLubricationList.isEmpty()) {
//            workHoursLubricationTableContainer = new VBox();
//            workHoursLubricationTableContainer.setVisible(false);
//
//            workHoursLubricationTableView = new TableView<>();
//            workHoursLubricationObservableList = FXCollections.observableArrayList();
//            workHoursLubricationTableView.setItems(workHoursLubricationObservableList);
//
//            adjustTableSize(workHoursLubricationTableView, workHoursLubricationTableContainer);
//            workHoursLubricationTableView.getStyleClass().add("table-view");
//
//            TableColumn<WorkHoursLubrication, String> lubricationPointColumn = new TableColumn<>(Constants.LUBRICATION_POINT_LABEL);
//            lubricationPointColumn.setCellValueFactory(new PropertyValueFactory<>("lubricationPoint"));
//
//            TableColumn<WorkHoursLubrication, String> lubricationMethodColumn = new TableColumn<>(Constants.LUBRICATION_POINT_LABEL);
//            lubricationMethodColumn.setCellValueFactory(new PropertyValueFactory<>("lubricationMethod"));
//
//            TableColumn<WorkHoursLubrication, Integer> workHoursColumn = new TableColumn<>(Constants.LUBRICATION_WORK_HOURS_LABEL);
//            workHoursColumn.setCellValueFactory(new PropertyValueFactory<>("workHours"));
//
//            TableColumn<WorkHoursLubrication, String> lubricantColumn = new TableColumn<>(Constants.LUBRICANT_LABEL);
//            lubricantColumn.setCellValueFactory(new PropertyValueFactory<>("lubricant"));
//
//            TableColumn<WorkHoursLubrication, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
//            additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));
//
//            workHoursLubricationTableView.getColumns().addAll(lubricationPointColumn, lubricationMethodColumn, workHoursColumn, lubricantColumn, additionalInfoColumn);
//
//            workHoursLubricationTableContainer.getChildren().add(workHoursLubricationTableView);
//
//            workHoursToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
//                if (newToggle != null) {
//                    updateTable(
//                            workHoursLubricationObservableList,
//                            workHoursLubricationTableView,
//                            () -> MaintenanceDAO.getWorkHoursLubricationByTechnicId(technic.getId(),
//                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
//                            )
//                    );
//                } else {
//                    workHoursLubricationTableContainer.setVisible(false);
//                }
//            });
//
//            mainContainer.getChildren().add(workHoursLubricationTableContainer);
//        }
//    }
//

//

//

//

//

//
//

//
//}


// JavaFX есть table view с observable list, у таблицы две колонки string, в этих ячейках нужно сделать так, чтобы умещалось несколько строк текста, если текст длинный, одна колонка int, колонки по ширине в соотношении 3:5:2. Необходимо также сделать так, чтобы для таблицы рассчитывалась высота по сумме высот всех ячеек


