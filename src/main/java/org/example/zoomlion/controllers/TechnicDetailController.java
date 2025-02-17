package org.example.zoomlion.controllers;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.zoomlion.Constants;
import org.example.zoomlion.models.Technic;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TechnicDetailController {

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private MaterialIconView backIconButton;

    @FXML
    private HBox toggleButtonContainer;

    @FXML
    private VBox tableContainer;

    @FXML
    private TableView<?> technicTable;

    private Technic technic;

    // Список ТО (тех. обслуживание) по моточасам
    private final List<Integer> maintenanceHours = Arrays.asList(100, 250, 500, 750);
    private ToggleGroup toggleGroup;

    public void setTechnic(Technic technic) {
        this.technic = technic;
        nameLabel.setText(technic.getName());

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.IMAGE_PATH + "no-image.png")));
        String fullPath = Constants.IMAGE_PATH + technic.getImage_path();
        InputStream imageStream = getClass().getResourceAsStream(fullPath);
        if (imageStream != null) {
            image = new Image(imageStream);
        }
        imageView.setImage(image);
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        toggleGroup = new ToggleGroup();
        createToggleButtons();

        // Обработчик смены выбранной кнопки
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                updateTable((ToggleButton) newToggle);
            } else {
                tableContainer.setVisible(false);
            }
        });
    }

    /**
     * Создание кнопок ТО (ТО100, ТО250, ТО500, ТО750) динамически
     */
    private void createToggleButtons() {
        for (Integer hours : maintenanceHours) {
            ToggleButton button = new ToggleButton("ТО" + hours);
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

//        @../images/ZTC250V-1.png
