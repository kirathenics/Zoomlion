package org.example.zoomlion.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AbstractValueMaintenance {
    private final SimpleStringProperty maintenanceObject;
    private final SimpleStringProperty workContent;
    private final SimpleIntegerProperty value;
    private final SimpleStringProperty additionalInfo;

    public AbstractValueMaintenance(String maintenanceObject, String workContent, int value, String additionalInfo) {
        this.maintenanceObject = new SimpleStringProperty(maintenanceObject);
        this.workContent = new SimpleStringProperty(workContent);
        this.value = new SimpleIntegerProperty(value);
        this.additionalInfo = new SimpleStringProperty(additionalInfo);
    }

    public String getMaintenanceObject() {
        return maintenanceObject.get();
    }

    public SimpleStringProperty maintenanceObjectProperty() {
        return maintenanceObject;
    }

    public String getWorkContent() {
        return workContent.get();
    }

    public SimpleStringProperty workContentProperty() {
        return workContent;
    }

    public int getValue() {
        return value.get();
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }

    public String getAdditionalInfo() {
        return additionalInfo.get();
    }

    public SimpleStringProperty additionalInfoProperty() {
        return additionalInfo;
    }
}
