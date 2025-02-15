module org.example.zoomlion {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.zoomlion to javafx.fxml;
    exports org.example.zoomlion;
}