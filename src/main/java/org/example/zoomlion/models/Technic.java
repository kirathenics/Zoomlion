package org.example.zoomlion.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Technic {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty image_path;
    private final SimpleIntegerProperty technic_model_id;

    public Technic(int id, String name, String image_path, int technic_model_id) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.image_path = new SimpleStringProperty(image_path);
        this.technic_model_id = new SimpleIntegerProperty(technic_model_id);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getImage_path() {
        return image_path.get();
    }

    public SimpleStringProperty image_pathProperty() {
        return image_path;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public int getTechnic_model_id() {
        return technic_model_id.get();
    }

    public SimpleIntegerProperty technic_model_idProperty() {
        return technic_model_id;
    }
}
