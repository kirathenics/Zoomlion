package org.example.zoomlion.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MileageInfo {
    private final SimpleStringProperty maintenance_object;
    private final SimpleStringProperty work_content;
    private final SimpleIntegerProperty mileage;
    private final SimpleStringProperty additional_info;

    public MileageInfo(String maintenance_object, String work_content, int mileage, String additional_info) {
        this.maintenance_object = new SimpleStringProperty(maintenance_object);
        this.work_content = new SimpleStringProperty(work_content);
        this.mileage = new SimpleIntegerProperty(mileage);
        this.additional_info = new SimpleStringProperty(additional_info);
    }

    public String getMaintenance_object() {
        return maintenance_object.get();
    }

    public SimpleStringProperty maintenance_objectProperty() {
        return maintenance_object;
    }

    public String getWork_content() {
        return work_content.get();
    }

    public SimpleStringProperty work_contentProperty() {
        return work_content;
    }

    public int getMileage() {
        return mileage.get();
    }

    public SimpleIntegerProperty mileageProperty() {
        return mileage;
    }

    public String getAdditional_info() {
        return additional_info.get();
    }

    public SimpleStringProperty additional_infoProperty() {
        return additional_info;
    }
}
