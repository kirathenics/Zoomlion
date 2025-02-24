package org.example.zoomlion.TechnicMaintenanceUIFactory;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.zoomlion.models.Technic;

public class DefaultMaintenanceUIFactory implements TechnicMaintenanceUIFactory {
    @Override
    public Node createUI(Technic technic) {
        VBox vbox = new VBox(10);

        vbox.getChildren().add(new Label("Нет информации"));

        return vbox;
    }
}
