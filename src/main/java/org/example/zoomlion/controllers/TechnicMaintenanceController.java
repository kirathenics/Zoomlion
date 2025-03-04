package org.example.zoomlion.controllers;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.zoomlion.DB.TechnicDAO;
import org.example.zoomlion.TechnicMaintenanceUIFactory.TechnicMaintenanceUIFactory;
import org.example.zoomlion.TechnicMaintenanceUIFactory.TechnicMaintenanceUIFactoryProvider;
import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.models.Technic;

import java.io.InputStream;
import java.util.Objects;

public class TechnicMaintenanceController {

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private MaterialIconView backIconButton;

    @FXML
    private VBox dynamicContentContainer;

    private Technic technic;

    public void setTechnic(Technic technic) {
        this.technic = technic;
        nameLabel.setText(technic.getName());

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.IMAGE_PATH + "no-image.png")));
        String fullPath = Constants.IMAGE_PATH + technic.getImagePath();
        InputStream imageStream = getClass().getResourceAsStream(fullPath);
        if (imageStream != null) {
            image = new Image(imageStream);
        }
        imageView.setImage(image);

        loadTechnicUI();
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.close();
    }

    private void loadTechnicUI() {
        dynamicContentContainer.getChildren().clear();
        TechnicMaintenanceUIFactory factory = TechnicMaintenanceUIFactoryProvider.getFactory(
                Objects.requireNonNull(
                        TechnicDAO.getTechnicTypeByTechnicId(technic.getId())
                )
        );
        dynamicContentContainer.getChildren().add(factory.createUI(technic));
    }
}
