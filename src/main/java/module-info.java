module org.example.zoomlion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.materialicons;
    requires org.json;


    opens org.example.zoomlion to javafx.fxml;
    opens org.example.zoomlion.controllers to javafx.fxml;
    exports org.example.zoomlion;
    exports org.example.zoomlion.utils;
    opens org.example.zoomlion.utils to javafx.fxml;
    exports org.example.zoomlion.TechnicMaintenanceUIFactory;
    opens org.example.zoomlion.TechnicMaintenanceUIFactory to javafx.fxml;
    opens org.example.zoomlion.models to javafx.base;
    exports org.example.zoomlion.models;
}