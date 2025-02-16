package org.example.zoomlion.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.zoomlion.models.Technic;

public class TechnicItemController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label nameLabel;

    private Technic technic;

    public void setData(Technic technic) {
        this.technic = technic;
        nameLabel.setText(technic.getName());
        Image image = new Image(getClass().getResourceAsStream("/org/example/zoomlion/images/ZTC250V-1.jpg"));
        imageView.setImage(image);
    }
}
