package org.example.zoomlion;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NextMaintenanceApp extends Application {

    private List<Integer> maintenanceIntervals;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        maintenanceIntervals = new ArrayList<>();
        Collections.addAll(maintenanceIntervals, 100, 250, 1000); // Интервалы обслуживания в км

        TextField mileageInput = new TextField();
        mileageInput.setPromptText("Введите текущий пробег (км)");

        Label resultLabel = new Label();

        Button checkButton = getCheckButton(mileageInput, resultLabel);

        VBox vbox = new VBox(10, mileageInput, checkButton, resultLabel);
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Определение следующего ТО");
        primaryStage.show();
    }

    private Button getCheckButton(TextField mileageInput, Label resultLabel) {
        Button checkButton = new Button("Определить следующее ТО");
        checkButton.setOnAction(event -> {
            String inputText = mileageInput.getText();
            try {
                int currentMileage = Integer.parseInt(inputText);
                Integer nextMaintenance = getNextMaintenance(currentMileage, maintenanceIntervals);
                if (nextMaintenance != null) {
                    resultLabel.setText("Следующее ТО на " + nextMaintenance + " км");
                } else {
                    resultLabel.setText("Все ТО выполнены");
                }
            } catch (NumberFormatException e) {
                resultLabel.setText("Введите корректное число");
            }
        });
        return checkButton;
    }

    private Integer getNextMaintenance(int currentMileage, List<Integer> maintenanceIntervals) {
        List<Integer> nextMaintenance = new ArrayList<>();

        for (int interval : maintenanceIntervals) {
            int prevMileage = currentMileage - currentMileage % interval;
            nextMaintenance.add(prevMileage + interval);
        }

        int minDiff = Integer.MAX_VALUE;
        Integer nextServiceInterval = null;

        for (int i = 0; i < nextMaintenance.size(); i++) {
            int diff = nextMaintenance.get(i) - currentMileage;

            if (diff >= 0 && diff <= minDiff) {
                minDiff = diff;
                nextServiceInterval = maintenanceIntervals.get(i);
            }
        }

        return nextServiceInterval;
    }

}