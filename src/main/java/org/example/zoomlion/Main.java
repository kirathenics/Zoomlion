package org.example.zoomlion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/main_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            String css = this.getClass().getResource("css/styles.css").toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setTitle("Zoomlion");
            primaryStage.setScene(scene);
//            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}