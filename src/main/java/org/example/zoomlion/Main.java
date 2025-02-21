package org.example.zoomlion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/main_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            String css = Objects.requireNonNull(getClass().getResource("css/styles.css")).toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setTitle("Zoomlion");
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("images/icon-app.png")).toExternalForm()));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}