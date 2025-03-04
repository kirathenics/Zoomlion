package org.example.zoomlion.TableViewFactory;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import org.example.zoomlion.TableColumnFactory.IntegerColumnFactory;
import org.example.zoomlion.TableColumnFactory.MultilineStringColumnFactory;
import org.example.zoomlion.TableViewFactory.TableBuilder.AbstractTableBuilder;
import org.example.zoomlion.Utils.Constants;

import java.util.Arrays;

public class MaintenanceTable<T> extends AbstractMaintenanceTable<T> {
    private TableColumn<T, String> maintenanceObjectColumn;
    private TableColumn<T, String> workContentColumn;
    private TableColumn<T, Integer> valueColumn;
    private TableColumn<T, String> additionalInfoColumn;

    private final String valueColumnLabel;

    public MaintenanceTable(String valueColumnLabel) {
        super();
        this.valueColumnLabel = valueColumnLabel;
        setupColumns();
    }

    @Override
    protected void setupColumns() {
        maintenanceObjectColumn = new MultilineStringColumnFactory<T>().createColumn(Constants.MAINTENANCE_OBJECT_LABEL, "maintenanceObject");
        workContentColumn = new MultilineStringColumnFactory<T>().createColumn(Constants.WORK_CONTENTS_LABEL, "workContent");
        valueColumn = new IntegerColumnFactory<T>().createColumn(valueColumnLabel, "value");
        additionalInfoColumn = new MultilineStringColumnFactory<T>().createColumn(Constants.ADDITIONAL_INFO_LABEL, "additionalInfo");

        tableView.getColumns().addAll(Arrays.asList(
                maintenanceObjectColumn,
                workContentColumn,
                valueColumn,
                additionalInfoColumn));
    }

    @Override
    protected void adjustColumnWidths(boolean hasAdditionalInfo) {
        double additionalInfoWidth = hasAdditionalInfo ? 0.2 : 0.0;
        double remainingWidth = 1.0 - additionalInfoWidth;

        double maintenanceObjectWidth = remainingWidth * 0.3;
        double workContentWidth = remainingWidth * 0.5;
        double valueWidth = remainingWidth * 0.18;

        maintenanceObjectColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(maintenanceObjectWidth));
        workContentColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(workContentWidth));
        valueColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(valueWidth));

        if (hasAdditionalInfo) {
            additionalInfoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(additionalInfoWidth));
        }
    }

    @Override
    protected AbstractMaintenanceTable<T> cloneTable() {
        MaintenanceTable<T> copy = new MaintenanceTable<>(this.valueColumnLabel);
        copy.updateTable(FXCollections.observableArrayList(this.observableList));
        return copy;
    }

    public static class Builder<T> extends AbstractTableBuilder<T, MaintenanceTable<T>> {
        @Override
        public MaintenanceTable<T> build() {
            return new MaintenanceTable<>(valueColumnLabel);
        }
    }
}
