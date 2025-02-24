package org.example.zoomlion.TableViewFactory.Builders;

import org.example.zoomlion.TableViewFactory.LubricationMaintenanceTable;

//public class LubricationMaintenanceTableBuilder<T> extends AbstractTableBuilder<T>  {
//    public LubricationMaintenanceTable<T> build() {
//        return new LubricationMaintenanceTable<>(valueColumnLabel, valueColumnProperty);
//    }
//}

public class LubricationMaintenanceTableBuilder<T> implements AbstractTableBuilder<T>  {
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

    public LubricationMaintenanceTable<T> build() {
        return new LubricationMaintenanceTable<>(valueColumnLabel, valueColumnProperty);
    }
}
