package org.example.zoomlion.TableViewFactory.Builders;

//public class AbstractTableBuilder<T> {
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
//}

public interface AbstractTableBuilder<T> {
    void setValueColumnLabel(String valueColumnLabel);
    void setValueColumnProperty(String valueColumnProperty);
}