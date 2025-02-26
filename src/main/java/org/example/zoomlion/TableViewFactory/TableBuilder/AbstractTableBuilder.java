package org.example.zoomlion.TableViewFactory.TableBuilder;

import org.example.zoomlion.TableViewFactory.AbstractMaintenanceTable;

public abstract class AbstractTableBuilder<T, R extends AbstractMaintenanceTable<T>> {
    protected String valueColumnLabel;
    protected String valueColumnProperty;

    public AbstractTableBuilder<T, R> setValueColumnLabel(String valueColumnLabel) {
        this.valueColumnLabel = valueColumnLabel;
        return this;
    }

    public AbstractTableBuilder<T, R> setValueColumnProperty(String valueColumnProperty) {
        this.valueColumnProperty = valueColumnProperty;
        return this;
    }

    public abstract R build();
}