package org.example.zoomlion.TableViewFactory;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import org.example.zoomlion.TableColumnFactory.IntegerColumnFactory;
import org.example.zoomlion.TableColumnFactory.MultilineStringColumnFactory;
import org.example.zoomlion.TableViewFactory.TableBuilder.AbstractTableBuilder;
import org.example.zoomlion.utils.Constants;

import java.util.Arrays;

public class LubricationMaintenanceTable<T> extends AbstractMaintenanceTable<T> {
    private TableColumn<T, String> lubricationPointColumn;
    private TableColumn<T, String> lubricationMethodColumn;
    private TableColumn<T, Integer> valueColumn;
    private TableColumn<T, String> lubricantColumn;
    private TableColumn<T, String> additionalInfoColumn;

    private final String valueColumnLabel;

    public LubricationMaintenanceTable(String valueColumnLabel) {
        super();
        this.valueColumnLabel = valueColumnLabel;
        setupColumns();
    }

    @Override
    protected void setupColumns() {
        lubricationPointColumn = new MultilineStringColumnFactory<T>().createColumn(Constants.LUBRICATION_POINT_LABEL, "lubricationPoint");
        lubricationMethodColumn = new MultilineStringColumnFactory<T>().createColumn(Constants.LUBRICATION_METHOD_LABEL, "lubricationMethod");
        valueColumn = new IntegerColumnFactory<T>().createColumn(valueColumnLabel, "value");
        lubricantColumn = new MultilineStringColumnFactory<T>().createColumn(Constants.LUBRICANT_LABEL, "lubricant");
        additionalInfoColumn = new MultilineStringColumnFactory<T>().createColumn(Constants.ADDITIONAL_INFO_LABEL, "additionalInfo");

        tableView.getColumns().addAll(Arrays.asList(
                lubricationPointColumn,
                lubricationMethodColumn,
                valueColumn,
                lubricantColumn,
                additionalInfoColumn));
    }

    @Override
    protected void adjustColumnWidths(boolean hasAdditionalInfo) {
        double additionalInfoWidth = hasAdditionalInfo ? 0.2 : 0.0;
        double remainingWidth = 1.0 - additionalInfoWidth;

        double maintenanceObjectWidth = remainingWidth * 0.3;
        double workContentWidth = remainingWidth * 0.2;
        double valueWidth = remainingWidth * 0.18;
        double lubricantWidth = remainingWidth * 0.3;

        lubricationPointColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(maintenanceObjectWidth));
        lubricationMethodColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(workContentWidth));
        valueColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(valueWidth));
        lubricantColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(lubricantWidth));

        if (hasAdditionalInfo) {
            additionalInfoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(additionalInfoWidth));
        }
    }

    @Override
    protected AbstractMaintenanceTable<T> cloneTable() {
        LubricationMaintenanceTable<T> copy = new LubricationMaintenanceTable<>(this.valueColumnLabel);
        copy.updateTable(FXCollections.observableArrayList(this.observableList));
        return copy;
    }

    public static class Builder<T> extends AbstractTableBuilder<T, LubricationMaintenanceTable<T>> {
        @Override
        public LubricationMaintenanceTable<T> build() {
            return new LubricationMaintenanceTable<>(valueColumnLabel);
        }
    }
}

