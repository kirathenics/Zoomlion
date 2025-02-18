package org.example.zoomlion.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MileageMaintenance {
    private final SimpleStringProperty maintenanceObject;
    private final SimpleStringProperty workContent;
    private final SimpleIntegerProperty mileage;
    private final SimpleStringProperty additionalInfo;

    public MileageMaintenance(String maintenanceObject, String workContent, int mileage, String additionalInfo) {
        this.maintenanceObject = new SimpleStringProperty(maintenanceObject);
        this.workContent = new SimpleStringProperty(workContent);
        this.mileage = new SimpleIntegerProperty(mileage);
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

    public int getMileage() {
        return mileage.get();
    }

    public SimpleIntegerProperty mileageProperty() {
        return mileage;
    }

    public String getAdditionalInfo() {
        return additionalInfo.get();
    }

    public SimpleStringProperty additionalInfoProperty() {
        return additionalInfo;
    }
}
