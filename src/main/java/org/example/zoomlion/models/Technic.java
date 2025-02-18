package org.example.zoomlion.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Technic {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty imagePath;
    private final SimpleIntegerProperty technicModelId;

    public Technic(int id, String name, String imagePath, int technicModelId) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.technicModelId = new SimpleIntegerProperty(technicModelId);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getImagePath() {
        return imagePath.get();
    }

    public SimpleStringProperty imagePathProperty() {
        return imagePath;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public int getTechnicModelId() {
        return technicModelId.get();
    }

    public SimpleIntegerProperty technicModelIdProperty() {
        return technicModelId;
    }
}
