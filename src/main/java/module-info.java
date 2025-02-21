module org.example.zoomlion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.materialicons;


    opens org.example.zoomlion to javafx.fxml;
    opens org.example.zoomlion.controllers to javafx.fxml;
    exports org.example.zoomlion;
    exports org.example.zoomlion.Utils;
    opens org.example.zoomlion.Utils to javafx.fxml;
    exports org.example.zoomlion.MaintenanceUIFactory;
    opens org.example.zoomlion.MaintenanceUIFactory to javafx.fxml;
    opens org.example.zoomlion.models to javafx.base;
    exports org.example.zoomlion.models;
}