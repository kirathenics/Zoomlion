package org.example.zoomlion.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.example.zoomlion.DB.DAO;
import org.example.zoomlion.models.Technic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private GridPane technicItemGridPane;

    @FXML
    private ScrollPane technicItemScrollPane;

    private List<Technic> technicList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        technicList.addAll(DAO.loadTechnic());

        technicItemScrollPane.widthProperty().addListener((obs, oldWidth, newWidth) -> updateGrid((double) newWidth));

        updateGrid(technicItemScrollPane.getWidth());
    }

    private void updateGrid(double width) {
        technicItemGridPane.getChildren().clear();

        int columnCount = (int) Math.max(1, width / 250.0);
        int column = 0, row = 1;

        try {
            for (Technic technic : technicList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/zoomlion/views/technic_item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                TechnicItemController technicItemController = fxmlLoader.getController();
                technicItemController.setData(technic);

                technicItemGridPane.add(anchorPane, column, row);
                GridPane.setMargin(anchorPane, new Insets(10));

                column++;
                if (column >= columnCount) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
