package org.example.zoomlion.TableColumnFactory;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.zoomlion.TableColumnFactory.Wrappers.WrappedColumnHeader;
import org.example.zoomlion.TableColumnFactory.Wrappers.WrappedTableCellFactory;

public class MultilineStringColumnFactory<T> extends StringColumnFactory<T>  {
    @Override
    public TableColumn<T, String> createColumn(String title, String property) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(tc -> WrappedTableCellFactory.createWrappedCell());
        WrappedColumnHeader.makeHeaderWrappable(column);
        return column;
    }
}
