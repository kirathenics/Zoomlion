package org.example.zoomlion.TableViewFactory.Builders;

import org.example.zoomlion.TableViewFactory.AbstractMaintenanceTable;

//public abstract class AbstractTableBuilder<T> {
//    protected String valueColumnLabel;
//    protected String valueColumnProperty;
//
//    public AbstractTableBuilder<T> setValueColumnLabel(String valueColumnLabel) {
//        this.valueColumnLabel = valueColumnLabel;
//        return this;
//    }
//
//    public AbstractTableBuilder<T> setValueColumnProperty(String valueColumnProperty) {
//        this.valueColumnProperty = valueColumnProperty;
//        return this;
//    }
//
//    public abstract AbstractMaintenanceTable<T> build();
//}

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

    public abstract R build(); // Метод должен создавать объект конкретного класса-наследника AbstractMaintenanceTable
}


//public interface AbstractTableBuilder<T> {
//    void setValueColumnLabel(String valueColumnLabel);
//    void setValueColumnProperty(String valueColumnProperty);
//}