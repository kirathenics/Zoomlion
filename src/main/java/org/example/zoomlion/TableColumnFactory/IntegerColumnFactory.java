package org.example.zoomlion.TableColumnFactory;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class IntegerColumnFactory<T> implements TableColumnFactory<T, Integer> {
    @Override
    public TableColumn<T, Integer> createColumn(String title, String property) {
        TableColumn<T, Integer> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }
}
