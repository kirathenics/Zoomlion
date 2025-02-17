package org.example.zoomlion.controllers;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/zoomlion/images/no-image-2.png")));
        String imagePath = "/org/example/zoomlion/images/" + technic.getImage_path();
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
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
