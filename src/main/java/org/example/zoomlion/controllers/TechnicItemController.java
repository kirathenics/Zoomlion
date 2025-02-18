package org.example.zoomlion.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.models.Technic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class TechnicItemController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label nameLabel;

    @FXML
    private AnchorPane rootPane;

    private Technic technic;

    public void setData(Technic technic) {
        this.technic = technic;
        nameLabel.setText(technic.getName());

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.IMAGE_PATH + "no-image.png")));

        String fullPath = Constants.IMAGE_PATH + technic.getImage_path();
        InputStream imageStream = getClass().getResourceAsStream(fullPath);
        if (imageStream != null) {
            image = new Image(imageStream);
        }

        imageView.setImage(image);

        rootPane.setOnMouseClicked(this::openDetailScene);
    }

    private void openDetailScene(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/zoomlion/views/technic_detail_view.fxml"));
            Parent detailRoot = loader.load();

            TechnicDetailController controller = loader.getController();
            controller.setTechnic(technic);

            Stage stage = new Stage();
            stage.setScene(new Scene(detailRoot));
            stage.setTitle(technic.getName());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}