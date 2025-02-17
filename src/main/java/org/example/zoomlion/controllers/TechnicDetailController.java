package org.example.zoomlion.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.zoomlion.models.Technic;

public class TechnicDetailController {
    @FXML
    private Label detailNameLabel;

    private Technic technic;

    public void setTechnic(Technic technic) {
        this.technic = technic;
        detailNameLabel.setText(technic.getName());
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) detailNameLabel.getScene().getWindow();
        stage.close();
    }
}
