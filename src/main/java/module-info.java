module org.example.zoomlion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.zoomlion to javafx.fxml;
    opens org.example.zoomlion.controllers to javafx.fxml;
    exports org.example.zoomlion;
}