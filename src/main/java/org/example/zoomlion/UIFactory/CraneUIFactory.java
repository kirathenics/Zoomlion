package org.example.zoomlion.UIFactory;

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

import java.util.List;

//public class CraneUIFactory implements TechnicUIFactory {
//    HBox toggleButtonContainer;
//    ToggleGroup toggleGroup;
//    VBox tableContainer;
//    TableView<?> technicTable;
//
//    @Override
//    public Node createUI(Technic technic) {
//        VBox vbox = new VBox(10);
//
//        List<Integer> mileageList = MaintenanceDAO.getMileageByTechnicId(technic.getId());
//
//        if (!mileageList.isEmpty()) {
//            vbox.getChildren().add(new Label("ТО по пробегу:"));
//
//            toggleButtonContainer = new HBox(10);
//            toggleGroup = new ToggleGroup();
//            createToggleButtons(toggleGroup, mileageList);
//
//            tableContainer = new VBox();
//            tableContainer.setVisible(false);
//
//            technicTable = new TableView<>();
//            technicTable.getColumns().add(new TableColumn<>("Объекты обслуживания"));
//            technicTable.getColumns().add(new TableColumn<>("Содержание работ"));
//            technicTable.getColumns().add(new TableColumn<>("Пробег, км"));
//            // опционально добавить доп инфу
//
//            tableContainer.getChildren().add(technicTable);
//
//            toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
//                if (newToggle != null) {
//                    updateTable((ToggleButton) newToggle);
//                } else {
//                    tableContainer.setVisible(false);
//                }
//            });
//
//            vbox.getChildren().add(toggleButtonContainer);
//            vbox.getChildren().add(tableContainer);
//        }
//
//        return vbox;
//    }
//
//    /**
//     * Создание кнопок ТО (ТО100, ТО250, ТО500, ТО750) динамически
//     */
//    private void createToggleButtons(ToggleGroup toggleGroup, List<Integer> mileageList) {
//        for (Integer mileage : mileageList) {
//            ToggleButton button = new ToggleButton("ТО" + mileage);
//            button.setToggleGroup(toggleGroup);
//            button.getStyleClass().add("toggle-button"); // Применяем стиль из styles.css
//            toggleButtonContainer.getChildren().add(button);
//        }
//    }
//
//    /**
//     * Обновление таблицы при выборе ТО
//     */
//    private void updateTable(ToggleButton selectedButton) {
//        tableContainer.setVisible(true);
//        String selectedTO = selectedButton.getText().replace("ТО", "");
//        loadData(selectedTO);
//    }
//
//    /**
//     * Загрузка данных в таблицу
//     */
//    private void loadData(String filter) {
//        System.out.println("Loading data for maintenance: " + filter);
//    }
//}

public class CraneUIFactory implements TechnicUIFactory {
    private HBox toggleButtonContainer;
    private ToggleGroup toggleGroup;
    private VBox tableContainer;
    private TableView<MileageMaintenance> technicTable;
    private ObservableList<MileageMaintenance> tableData;

    @Override
    public Node createUI(Technic technic) {
        VBox vbox = new VBox(10);

        List<Integer> mileageList = MaintenanceDAO.getMileageByTechnicId(technic.getId());
        System.out.println(mileageList);

        if (!mileageList.isEmpty()) {
            vbox.getChildren().add(new Label("ТО по пробегу:"));

            toggleButtonContainer = new HBox(10);
            toggleGroup = new ToggleGroup();
            createToggleButtons(toggleGroup, mileageList);

            tableContainer = new VBox();
            tableContainer.setVisible(false);

            technicTable = new TableView<>();
            tableData = FXCollections.observableArrayList();
            technicTable.setItems(tableData);

            TableColumn<MileageMaintenance, String> maintenanceObjectColumn = new TableColumn<>("Объекты обслуживания");
            maintenanceObjectColumn.setCellValueFactory(new PropertyValueFactory<>("maintenanceObject"));

            TableColumn<MileageMaintenance, String> workContentColumn = new TableColumn<>("Содержание работ");
            workContentColumn.setCellValueFactory(new PropertyValueFactory<>("workContent"));

            TableColumn<MileageMaintenance, Integer> mileageColumn = new TableColumn<>("Пробег, км");
            mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));

            TableColumn<MileageMaintenance, String> additionalInfoColumn = new TableColumn<>("Доп. информация");
            additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));

            technicTable.getColumns().addAll(maintenanceObjectColumn, workContentColumn, mileageColumn, additionalInfoColumn);

            tableContainer.getChildren().add(technicTable);

            toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    updateTable((ToggleButton) newToggle, technic.getId());
                } else {
                    tableContainer.setVisible(false);
                }
            });

            vbox.getChildren().addAll(toggleButtonContainer, tableContainer);
        }

        return vbox;
    }

    private void createToggleButtons(ToggleGroup toggleGroup, List<Integer> mileageList) {
        for (Integer mileage : mileageList) {
            ToggleButton button = new ToggleButton("ТО" + mileage);
            button.setToggleGroup(toggleGroup);
            button.getStyleClass().add("toggle-button");
            toggleButtonContainer.getChildren().add(button);
        }
    }

    private void updateTable(ToggleButton selectedButton, int technicId) {
        tableContainer.setVisible(true);
        String selectedTO = selectedButton.getText().replace("ТО", "");
        loadData(Integer.parseInt(selectedTO), technicId);
    }

    private void loadData(int mileage, int technicId) {
        tableData.clear();
        List<MileageMaintenance> maintenanceList = MaintenanceDAO.loadMileageMaintenanceByTechnicId(technicId)
                .stream()
                .filter(m -> m.getMileage() == mileage)
                .toList();

        tableData.addAll(maintenanceList);

        // Проверяем, есть ли хотя бы одно ненулевое значение в additionalInfo
        boolean hasAdditionalInfo = maintenanceList.stream()
                .anyMatch(m -> m.getAdditionalInfo() != null && !m.getAdditionalInfo().isEmpty());

        if (!hasAdditionalInfo) {
            // Убираем колонку, если в данных нет информации
            technicTable.getColumns().removeIf(col -> col.getText().equals("Доп. информация"));
        } else {
            // Добавляем колонку только если её нет в таблице
            if (technicTable.getColumns().stream().noneMatch(col -> col.getText().equals("Доп. информация"))) {
                TableColumn<MileageMaintenance, String> additionalInfoColumn = new TableColumn<>("Доп. информация");
                additionalInfoColumn.setCellValueFactory(cellData -> cellData.getValue().additionalInfoProperty());
                technicTable.getColumns().add(additionalInfoColumn);
            }
        }
    }
}
