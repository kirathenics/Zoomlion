package org.example.zoomlion.TableViewFactory;

import javafx.scene.control.TableColumn;
import org.example.zoomlion.TableColumnFactory.IntegerColumnFactory;
import org.example.zoomlion.TableColumnFactory.MultiLineStringColumnFactory;
import org.example.zoomlion.TableViewFactory.TableBuilder.AbstractTableBuilder;
import org.example.zoomlion.Utils.Constants;

import java.util.Arrays;

public class MaintenanceTable<T> extends AbstractMaintenanceTable<T> {
    private TableColumn<T, String> maintenanceObjectColumn;
    private TableColumn<T, String> workContentColumn;
    private TableColumn<T, Integer> valueColumn; // универсальный столбец
    private TableColumn<T, String> additionalInfoColumn;

    private final String valueColumnLabel;
    private final String valueColumnProperty;

    public MaintenanceTable(String valueColumnLabel, String valueColumnProperty) {
        super();
        this.valueColumnLabel = valueColumnLabel;
        this.valueColumnProperty = valueColumnProperty;
        setupColumns();
    }

    @Override
    protected void setupColumns() {
        maintenanceObjectColumn = new MultiLineStringColumnFactory<T>().createColumn(Constants.MAINTENANCE_OBJECT_LABEL, "maintenanceObject");
        workContentColumn = new MultiLineStringColumnFactory<T>().createColumn(Constants.WORK_CONTENTS_LABEL, "workContent");
        valueColumn = new IntegerColumnFactory<T>().createColumn(valueColumnLabel, valueColumnProperty);
        additionalInfoColumn = new MultiLineStringColumnFactory<T>().createColumn(Constants.ADDITIONAL_INFO_LABEL, "additionalInfo");

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
        double valueWidth = remainingWidth * 0.17;

        maintenanceObjectColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(maintenanceObjectWidth));
        workContentColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(workContentWidth));
        valueColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(valueWidth));

        if (hasAdditionalInfo) {
            additionalInfoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(additionalInfoWidth));
        }
    }

    public static class Builder<T> extends AbstractTableBuilder<T, MaintenanceTable<T>> {
        @Override
        public MaintenanceTable<T> build() {
            return new MaintenanceTable<>(valueColumnLabel, valueColumnProperty);
        }
    }
}


