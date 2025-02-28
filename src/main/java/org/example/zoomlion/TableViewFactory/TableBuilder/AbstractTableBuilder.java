package org.example.zoomlion.TableViewFactory.TableBuilder;

import org.example.zoomlion.TableViewFactory.AbstractMaintenanceTable;

public abstract class AbstractTableBuilder<T, R extends AbstractMaintenanceTable<T>> {
    protected String valueColumnLabel;

    public AbstractTableBuilder<T, R> setValueColumnLabel(String valueColumnLabel) {
        this.valueColumnLabel = valueColumnLabel;
        return this;
    }

    public abstract R build();
}