package org.example.zoomlion.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MaintenanceValue implements Comparable<MaintenanceValue> {
    private final SimpleIntegerProperty value;
    private final Boolean isPeriodic; // Может быть null

    public MaintenanceValue(int value, Boolean isPeriodic) {
        this.value = new SimpleIntegerProperty(value);
        this.isPeriodic = isPeriodic;
    }

    public int getValue() {
        return value.get();
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }

    public Boolean isPeriodic() {
        return isPeriodic;
    }

    public SimpleBooleanProperty isPeriodicProperty() {
        return isPeriodic != null ? new SimpleBooleanProperty(isPeriodic) : null;
    }

    @Override
    public String toString() {
        return "MaintenanceValue{" +
                "value=" + value +
                ", isPeriodic=" + isPeriodic +
                '}';
    }

    @Override
    public int compareTo(MaintenanceValue other) {
        return Integer.compare(this.getValue(), other.getValue());
    }
}