package org.example.zoomlion.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.example.zoomlion.DB.DAO;
import org.example.zoomlion.models.Technic;

import java.io.File;
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
        int column = 0;
        int row = 0;

        try {
            for (Technic technic : technicList) {
//                FXMLLoader fxmlLoader = new FXMLLoader();
//                fxmlLoader.setLocation(getClass().getResource("views/technic_item.fxml"));

//                URL resource = getClass().getClassLoader().getResource("views/technic_item.fxml");
//
//                if (resource == null) {
//                    throw new RuntimeException("FXML file not found: views/technic_item.fxml");
//                }
//
//                fxmlLoader.setLocation(resource);

//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/technic_item.fxml"));
                FXMLLoader fxmlLoader = new FXMLLoader(new File("src/main/resources/org/example/zoomlion/views/technic_item.fxml").toURI().toURL());

                AnchorPane anchorPane = fxmlLoader.load();

                TechnicItemController technicItemController = fxmlLoader.getController();
                technicItemController.setData(technic);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                technicItemGridPane.add(anchorPane, column++, row);

                technicItemGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                technicItemGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                technicItemGridPane.setMaxWidth(Region.USE_PREF_SIZE);

                technicItemGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                technicItemGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                technicItemGridPane.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
