package org.example.zoomlion.TableColumnFactory;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class StringColumnFactory<T> implements TableColumnFactory<T, String> {
    @Override
    public TableColumn<T, String> createColumn(String title, String property) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }
}