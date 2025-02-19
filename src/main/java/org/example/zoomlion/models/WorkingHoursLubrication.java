package org.example.zoomlion.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WorkingHoursLubrication {
    private final SimpleStringProperty lubricationPoint;
    private final SimpleStringProperty lubricationMethod;
    private final SimpleIntegerProperty workingHours;
    private final SimpleStringProperty lubricant;
    private final SimpleStringProperty additionalInfo;

    public WorkingHoursLubrication(String lubricationPoint, String lubricationMethod, int workingHours, String lubricant, String additionalInfo) {
        this.lubricationPoint = new SimpleStringProperty(lubricationPoint);
        this.lubricationMethod = new SimpleStringProperty(lubricationMethod);
        this.workingHours = new SimpleIntegerProperty(workingHours);
        this.lubricant = new SimpleStringProperty(lubricant);
        this.additionalInfo = new SimpleStringProperty(additionalInfo);
    }

    public String getLubricationPoint() {
        return lubricationPoint.get();
    }

    public SimpleStringProperty lubricationPointProperty() {
        return lubricationPoint;
    }

    public String getLubricationMethod() {
        return lubricationMethod.get();
    }

    public SimpleStringProperty lubricationMethodProperty() {
        return lubricationMethod;
    }

    public int getWorkingHours() {
        return workingHours.get();
    }

    public SimpleIntegerProperty workingHoursProperty() {
        return workingHours;
    }

    public String getLubricant() {
        return lubricant.get();
    }

    public SimpleStringProperty lubricantProperty() {
        return lubricant;
    }

    public String getAdditionalInfo() {
        return additionalInfo.get();
    }

    public SimpleStringProperty additionalInfoProperty() {
        return additionalInfo;
    }
}
