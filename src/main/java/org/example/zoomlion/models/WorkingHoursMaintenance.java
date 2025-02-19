package org.example.zoomlion.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WorkingHoursMaintenance {
    private final SimpleStringProperty maintenanceObject;
    private final SimpleStringProperty workContent;
    private final SimpleIntegerProperty workingHours;
    private final SimpleStringProperty additionalInfo;

    public WorkingHoursMaintenance(String maintenanceObject, String workContent, int workingHours, String additionalInfo) {
        this.maintenanceObject = new SimpleStringProperty(maintenanceObject);
        this.workContent = new SimpleStringProperty(workContent);
        this.workingHours = new SimpleIntegerProperty(workingHours);
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

    public int getWorkingHours() {
        return workingHours.get();
    }

    public SimpleIntegerProperty workingHoursProperty() {
        return workingHours;
    }

    public String getAdditionalInfo() {
        return additionalInfo.get();
    }

    public SimpleStringProperty additionalInfoProperty() {
        return additionalInfo;
    }
}
