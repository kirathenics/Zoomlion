package org.example.zoomlion.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WorkHoursMaintenance {
    private final SimpleStringProperty maintenanceObject;
    private final SimpleStringProperty workContent;
    private final SimpleIntegerProperty workHours;
    private final SimpleStringProperty additionalInfo;

    public WorkHoursMaintenance(String maintenanceObject, String workContent, int workHours, String additionalInfo) {
        this.maintenanceObject = new SimpleStringProperty(maintenanceObject);
        this.workContent = new SimpleStringProperty(workContent);
        this.workHours = new SimpleIntegerProperty(workHours);
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

    public int getWorkHours() {
        return workHours.get();
    }

    public SimpleIntegerProperty workHoursProperty() {
        return workHours;
    }

    public String getAdditionalInfo() {
        return additionalInfo.get();
    }

    public SimpleStringProperty additionalInfoProperty() {
        return additionalInfo;
    }
}
