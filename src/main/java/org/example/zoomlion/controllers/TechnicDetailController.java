package org.example.zoomlion.controllers;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.zoomlion.Constants;
import org.example.zoomlion.models.Technic;

import java.io.InputStream;
import java.util.Objects;

public class TechnicDetailController {

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private MaterialIconView backIconButton;

    private Technic technic;

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
}

//        @../images/ZTC250V-1.png
