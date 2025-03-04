package org.example.zoomlion.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.example.zoomlion.DB.TechnicDAO;
import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.Utils.UserDialogs;
import org.example.zoomlion.models.Technic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML
    private FontAwesomeIconView homeSidebarButton;

    @FXML
    private FontAwesomeIconView settingsSidebarButton;

    @FXML
    private FontAwesomeIconView aboutSidebarButton;

    @FXML
    private GridPane technicItemGridPane;

    @FXML
    private ScrollPane technicItemScrollPane;

    private final String ACTIVE_COLOR = "#A4CE4E";
    private final String DEFAULT_COLOR = "#C1C1C1";

    private final List<Technic> technicList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        technicList.addAll(TechnicDAO.loadTechnic());
        technicItemScrollPane.widthProperty().addListener((obs, oldWidth, newWidth) -> updateGrid((double) newWidth));

        updateGrid(technicItemScrollPane.getWidth());

//        String updateUrl = GitHubUpdateChecker.checkForUpdates();
//        if (updateUrl != null) {
//            updateProjectCircle.setVisible(true);
//            updateProjectCircle.setManaged(true);
//        }
//        else {
//            updateProjectCircle.setVisible(false);
//            updateProjectCircle.setManaged(false);
//        }

        homeSidebarButton.setOnMouseClicked(mouseEvent -> setActiveIcon(homeSidebarButton));

        settingsSidebarButton.setOnMouseClicked(mouseEvent -> setActiveIcon(settingsSidebarButton));

//        updateProjectSidebarButton.setOnMouseClicked(mouseEvent -> {
//            if (updateUrl != null) {
//                setActiveIcon(updateProjectSidebarButton);
//                updateProjectCircle.setVisible(false);
//                updateProjectCircle.setManaged(false);
////            UserDialogs.showInfo("Загрузка", "Идет загрузка...0%");
//                GitHubUpdater.downloadUpdate(updateUrl, "Zoomlion.jar", downloadProgressBar);
//                AppRestarter.restartApplication("Zoomlion.jar");
//                setActiveIcon(homeSidebarButton);
//            }
//        });

        aboutSidebarButton.setOnMouseClicked(mouseEvent -> {
            setActiveIcon(aboutSidebarButton);
            UserDialogs.showInfo("Информация", "Данная программа содержит справочную информацию о техническом обслуживании спецтехники Zoomlion.");
            setActiveIcon(homeSidebarButton);
        });

        setActiveIcon(homeSidebarButton);
    }

    private void updateGrid(double width) {
        technicItemGridPane.getChildren().clear();

        int columnCount = (int) Math.max(1, width / 250.0);
        int column = 0, row = 1;

        try {
            for (Technic technic : technicList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.VIEW_PATH + "technic_item_view.fxml"));
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

    private void setActiveIcon(FontAwesomeIconView activeIcon) {
        homeSidebarButton.setFill(Color.web(DEFAULT_COLOR));
        settingsSidebarButton.setFill(Color.web(DEFAULT_COLOR));
        aboutSidebarButton.setFill(Color.web(DEFAULT_COLOR));

        activeIcon.setFill(Color.web(ACTIVE_COLOR));
    }
}
