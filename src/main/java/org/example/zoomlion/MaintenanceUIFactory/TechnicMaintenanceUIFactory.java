package org.example.zoomlion.MaintenanceUIFactory;

import javafx.scene.Node;
import org.example.zoomlion.models.Technic;

public interface TechnicMaintenanceUIFactory {
    Node createUI(Technic technic);
}
