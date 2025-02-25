package org.example.zoomlion.TechnicMaintenanceUIFactory;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.example.zoomlion.MaintenanceUI.MileageMaintenanceUI;
import org.example.zoomlion.MaintenanceUI.WorkHoursMaintenanceUI;
import org.example.zoomlion.models.*;

public class CraneMaintenanceUIFactory implements TechnicMaintenanceUIFactory {

    @Override
    public Node createUI(Technic technic) {
        VBox mainContainer = new VBox();
        mainContainer.setPrefWidth(870);

        mainContainer.getChildren().add(new MileageMaintenanceUI(technic).getUI());
        mainContainer.getChildren().add(new WorkHoursMaintenanceUI(technic).getUI());

        return mainContainer;
    }
}