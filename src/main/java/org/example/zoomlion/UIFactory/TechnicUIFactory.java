package org.example.zoomlion.UIFactory;

import javafx.scene.Node;
import org.example.zoomlion.models.Technic;

public interface TechnicUIFactory {
    Node createUI(Technic technic);
}
