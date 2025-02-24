package org.example.zoomlion.TableViewFactory.Builders;

import org.example.zoomlion.TableViewFactory.MaintenanceTable;

public class MaintenanceTableBuilder<T> implements AbstractTableBuilder<T> {
    private String valueColumnLabel;
    private String valueColumnProperty;

    @Override
    public void setValueColumnLabel(String valueColumnLabel) {
        this.valueColumnLabel = valueColumnLabel;
    }

    @Override
    public void setValueColumnProperty(String valueColumnProperty) {
        this.valueColumnProperty = valueColumnProperty;
    }

    public MaintenanceTable<T> build() {
        return new MaintenanceTable<>(valueColumnLabel, valueColumnProperty);
    }
}
