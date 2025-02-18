package org.example.zoomlion.UIFactory;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.zoomlion.DB.MaintenanceDAO;
import org.example.zoomlion.models.Technic;

import java.util.List;

public class CraneUIFactory implements TechnicUIFactory {
    HBox toggleButtonContainer;
    ToggleGroup toggleGroup;
    VBox tableContainer;
    TableView<?> technicTable;

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
            technicTable.getColumns().add(new TableColumn<>("Объекты обслуживания"));
            technicTable.getColumns().add(new TableColumn<>("Содержание работ"));
            technicTable.getColumns().add(new TableColumn<>("Пробег, км"));
            // опционально добавить доп инфу

            tableContainer.getChildren().add(technicTable);

            toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    updateTable((ToggleButton) newToggle);
                } else {
                    tableContainer.setVisible(false);
                }
            });

            vbox.getChildren().add(toggleButtonContainer);
            vbox.getChildren().add(tableContainer);
        }

        return vbox;
    }

    /**
     * Создание кнопок ТО (ТО100, ТО250, ТО500, ТО750) динамически
     */
    private void createToggleButtons(ToggleGroup toggleGroup, List<Integer> mileageList) {
        for (Integer mileage : mileageList) {
            ToggleButton button = new ToggleButton("ТО" + mileage);
            button.setToggleGroup(toggleGroup);
            button.getStyleClass().add("toggle-button"); // Применяем стиль из styles.css
            toggleButtonContainer.getChildren().add(button);
        }
    }

    /**
     * Обновление таблицы при выборе ТО
     */
    private void updateTable(ToggleButton selectedButton) {
        tableContainer.setVisible(true);
        String selectedTO = selectedButton.getText().replace("ТО", "");
        loadData(selectedTO);
    }

    /**
     * Загрузка данных в таблицу
     */
    private void loadData(String filter) {
        System.out.println("Loading data for maintenance: " + filter);
    }
}
