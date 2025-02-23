package org.example.zoomlion.MaintenanceUIFactory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.zoomlion.DB.MaintenanceDAO;
import org.example.zoomlion.TableColumnFactory.IntegerColumnFactory;
import org.example.zoomlion.TableColumnFactory.MultiLineStringColumnFactory;
import org.example.zoomlion.TableColumnFactory.TableWrappedCellFactory;
import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.Utils.ListUtils;
import org.example.zoomlion.models.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;


// 335 code lines
// 2092 all


public class CraneMaintenanceUIFactory implements TechnicMaintenanceUIFactory {
    private VBox mainContainer;

    private HBox mileageToggleButtonContainer;
    private ToggleGroup mileageToggleGroup;

    private VBox mileageMaintenanceTableContainer;
    private TableView<MileageMaintenance> mileageMaintenanceTableView;
    private ObservableList<MileageMaintenance> mileageMaintenanceObservableList;

    TableColumn<MileageMaintenance, String> maintenanceObjectColumnMileageMaintenance;
    TableColumn<MileageMaintenance, String> workContentColumnMileageMaintenance;
    TableColumn<MileageMaintenance, Integer> mileageColumnMileageMaintenance;
    TableColumn<MileageMaintenance, String> additionalInfoColumnMileageMaintenance;

    private VBox mileageLubricationTableContainer;
    private TableView<MileageLubrication> mileageLubricationTableView;
    private ObservableList<MileageLubrication> mileageLubricationObservableList;


    private HBox workHoursToggleButtonContainer;
    private ToggleGroup workHoursToggleGroup;

    private VBox workHoursMaintenanceTableContainer;
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
        mainContainer.setPrefWidth(870);

        createMileageMaintenanceUI();
//        createWorkHoursMaintenanceUI();

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
            mileageMaintenanceTableContainer = new VBox();

            mileageMaintenanceTableContainer.prefWidthProperty().bind(mainContainer.widthProperty());
            mileageMaintenanceTableContainer.setMaxWidth(Double.MAX_VALUE);

            mileageMaintenanceTableView = new TableView<>();
            mileageMaintenanceObservableList = FXCollections.observableArrayList();
            mileageMaintenanceTableView.setItems(mileageMaintenanceObservableList);

            adjustTableSize(mileageMaintenanceTableView, mileageMaintenanceTableContainer);
            mileageMaintenanceTableView.getStyleClass().add("table-view");

//            maintenanceObjectColumnMileageMaintenance = createMaintenanceObjectTableColumn();
//            workContentColumnMileageMaintenance = createWorkContentTableColumn();
//            mileageColumnMileageMaintenance = createMileageTableColumn();
//            additionalInfoColumnMileageMaintenance= createAdditionalInfoTableColumn();

            maintenanceObjectColumnMileageMaintenance = new MultiLineStringColumnFactory<MileageMaintenance>().createColumn(Constants.MAINTENANCE_OBJECT_LABEL, "maintenanceObject");
            workContentColumnMileageMaintenance = new MultiLineStringColumnFactory<MileageMaintenance>().createColumn(Constants.WORK_CONTENTS_LABEL, "workContent");
            mileageColumnMileageMaintenance = new IntegerColumnFactory<MileageMaintenance>().createColumn(Constants.MILEAGE_LABEL, "mileage");
            additionalInfoColumnMileageMaintenance= new MultiLineStringColumnFactory<MileageMaintenance>().createColumn(Constants.ADDITIONAL_INFO_LABEL, "additionalInfo");

            mileageMaintenanceTableView.getColumns().addAll(
                    maintenanceObjectColumnMileageMaintenance,
                    workContentColumnMileageMaintenance,
                    mileageColumnMileageMaintenance,
                    additionalInfoColumnMileageMaintenance);

            mileageMaintenanceTableContainer.getChildren().add(mileageMaintenanceTableView);

            mileageToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    updateTable(
                            mileageMaintenanceObservableList,
                            mileageMaintenanceTableView,
                            () -> MaintenanceDAO.getMileageMaintenanceByTechnicId(technic.getId(),
                                    Integer.parseInt(((ToggleButton) newToggle).getText().replace(Constants.TO_LABEL, ""))
                            )
                    );
                } else {
                    mileageMaintenanceTableContainer.setVisible(false);
                    mileageMaintenanceTableContainer.setManaged(false);
                    mileageMaintenanceObservableList.clear();
                }
                adjustTableSize(mileageMaintenanceTableView, mileageMaintenanceTableContainer);
            });

            mainContainer.getChildren().add(mileageMaintenanceTableContainer);
        }

        //
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
    private <T> void updateTable(ObservableList<T> observableList, TableView<T> tableView,
                                 Supplier<List<T>> dataLoader) {
        observableList.clear();
        observableList.addAll(dataLoader.get());

        setAdditionalInfoColumn(observableList, tableView);
    }

    private <T> void setAdditionalInfoColumn(ObservableList<T> observableList, TableView<T> tableView) {
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

        adjustColumnWidths(tableView, hasAdditionalInfo);
    }

    private <T> TableColumn<T, String> getAdditionalInfoColumn() {
        TableColumn<T, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
        additionalInfoColumn.setCellValueFactory(cellData -> {
            try {
                Method method = cellData.getValue().getClass().getMethod("additionalInfoProperty");
                return (ObservableValue<String>) method.invoke(cellData.getValue());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return new SimpleStringProperty("");
            }
        });
        additionalInfoColumn.setCellFactory(tc -> TableWrappedCellFactory.createWrappedCell());
        return additionalInfoColumn;
    }

    private void adjustColumnWidths(TableView<?> tableView, boolean hasAdditionalInfo) {
        double additionalInfoWidth = hasAdditionalInfo ? 0.2 : 0.0;
        double remainingWidth = 1.0 - additionalInfoWidth;

        double maintenanceObjectWidth = remainingWidth * 0.3;
        double workContentWidth = remainingWidth * 0.5;
        double mileageWidth = remainingWidth * 0.2;

        maintenanceObjectColumnMileageMaintenance.prefWidthProperty().bind(tableView.widthProperty().multiply(maintenanceObjectWidth));
        workContentColumnMileageMaintenance.prefWidthProperty().bind(tableView.widthProperty().multiply(workContentWidth));
        mileageColumnMileageMaintenance.prefWidthProperty().bind(tableView.widthProperty().multiply(mileageWidth));

        if (hasAdditionalInfo) {
            additionalInfoColumnMileageMaintenance.prefWidthProperty().bind(tableView.widthProperty().multiply(additionalInfoWidth));
        }
    }

    private void adjustTableSize(TableView<?> tableView, VBox tableContainer) {
        tableView.setFixedCellSize(-1);

//        tableView.setFixedCellSize(30);
//        tableView.prefHeightProperty().bind(
//                Bindings.when(Bindings.isEmpty(tableView.getItems()))
//                        .then(0)
//                        .otherwise(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(30))
//        );

//        tableView.prefHeightProperty().bind(
//                Bindings.createDoubleBinding(() -> {
//                    int rowCount = tableView.getItems().size();
//                    double rowHeight = 30; // Примерная высота одной строки
//                    double headerHeight = 40; // Высота заголовка
//                    return rowCount > 0 ? (rowCount * rowHeight + headerHeight) : headerHeight;
//                }, tableView.getItems())
//        );
        tableView.setPrefHeight(450);
        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());


        tableView.getItems().addListener((ListChangeListener<? super Object>) change -> {
            tableView.refresh();
        });

        tableView.prefWidthProperty().bind(tableContainer.widthProperty());

        tableView.getItems().addListener((ListChangeListener<? super Object>) change -> {
            boolean hasItems = !tableView.getItems().isEmpty();
            tableContainer.setVisible(hasItems);
            tableContainer.setManaged(hasItems);
        });

        boolean hasItems = !tableView.getItems().isEmpty();
        tableContainer.setVisible(hasItems);
        tableContainer.setManaged(hasItems);
    }

//    private <T> TableCell<T, String> createWrappedCell() {
//        return new TableCell<>() {
//            private final Text text = new Text();
//
//            {
//                text.wrappingWidthProperty().bind(widthProperty());
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setGraphic(null);
//                    setPrefHeight(0);
//                } else {
//                    text.setText(item);
//                    setGraphic(text);
//                    setPrefHeight(text.getLayoutBounds().getHeight() + 10);
//                }
//            }
//        };
//    }
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


