package org.example.zoomlion.TableColumnFactory;

import javafx.scene.control.TableColumn;

public interface TableColumnFactory<T, V> {
    TableColumn<T, V> createColumn(String title, String property);
}
