package org.example.zoomlion.TableColumnFactory;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

public class MultiLineStringColumnFactory<T> extends StringColumnFactory<T>  {
    @Override
    public TableColumn<T, String> createColumn(String title, String property) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(tc -> WrappedTableCellFactory.createWrappedCell());
//        makeHeaderWrappable(column);
        return column;
    }

//    private void makeHeaderWrappable(TableColumn<T, String> col) {
//        Label label = new Label(col.getText());
//        label.setStyle("-fx-padding: 8px;");
//        label.setWrapText(true);
//        label.setAlignment(Pos.CENTER);
//        label.setTextAlignment(TextAlignment.CENTER);
//
//        StackPane stack = new StackPane();
//        stack.getChildren().add(label);
//        stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
//        label.prefWidthProperty().bind(stack.prefWidthProperty());
//        col.setText(null);
//        col.setGraphic(stack);
//    }
}
