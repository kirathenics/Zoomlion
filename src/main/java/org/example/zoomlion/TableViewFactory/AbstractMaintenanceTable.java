package org.example.zoomlion.TableViewFactory;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.VBox;
import org.example.zoomlion.TableColumnFactory.WrappedTableCellFactory;
import org.example.zoomlion.Utils.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractMaintenanceTable<T> {
    protected VBox tableContainer;
    protected TableView<T> tableView;
    protected ObservableList<T> observableList;

    public AbstractMaintenanceTable() {
        tableContainer = new VBox();
        VBox.setMargin(tableContainer, new Insets(10,0,10,0));

        tableView = new TableView<>();
        observableList = FXCollections.observableArrayList();
        tableView.setItems(observableList);

        setupTableSize();
        tableView.getStyleClass().add("maintenance-table-view");

        tableContainer.getChildren().add(tableView);
    }

    public VBox getTableContainer() {
        return tableContainer;
    }

    protected abstract void setupColumns();

    public void setupTableSize() {
        tableView.getItems().addListener((ListChangeListener<? super Object>) change -> tableView.refresh());

        tableView.getItems().addListener((ListChangeListener<? super Object>) change -> {
            boolean hasItems = !tableView.getItems().isEmpty();
            changeTableVisibility(hasItems);
        });

        boolean hasItems = !tableView.getItems().isEmpty();
        changeTableVisibility(hasItems);
    }

    public void adjustTableSize() {
        tableView.setFixedCellSize(-1);

        tableView.layout();

        TableViewSkin<?> skin = (TableViewSkin<?>) tableView.getSkin();
        if (skin != null) {
            VirtualFlow<?> flow = (VirtualFlow<?>) skin.getChildren().get(1);
            double totalHeight = tableView.lookup(".column-header-background").getBoundsInLocal().getHeight();
            totalHeight += 20;

            for (int i = 0; i < tableView.getItems().size(); i++) {
                totalHeight += flow.getCell(i).getBoundsInLocal().getHeight();
            }

            tableView.setPrefHeight(totalHeight);
        }

        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());

        tableView.prefWidthProperty().bind(tableContainer.widthProperty());
    }

    public void hideTable() {
        changeTableVisibility(false);
        observableList.clear();
    }

    private void changeTableVisibility(boolean shouldShowTable) {
        tableContainer.setVisible(shouldShowTable);
        tableContainer.setManaged(shouldShowTable);
    }

    /**
     * Обновление таблицы при выборе ТО
     */
    public void updateTable(Supplier<List<T>> dataSupplier) {
        observableList.setAll(dataSupplier.get());

        setAdditionalInfoColumn();

        Platform.runLater(this::adjustTableSize);
    }

    protected void setAdditionalInfoColumn() {
        boolean hasAdditionalInfo = observableList.stream().anyMatch(item -> {
            try {
                Method method = item.getClass().getMethod("getAdditionalInfo");
                Object value = method.invoke(item);
                return value != null && !value.toString().isEmpty();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return false;
            }
        });

        tableView.getColumns().removeIf(col -> Constants.ADDITIONAL_INFO_LABEL.equals(col.getText()));

        if (hasAdditionalInfo) {
            TableColumn<T, String> additionalInfoColumn = getAdditionalInfoColumn();
            tableView.getColumns().add(additionalInfoColumn);
        }

        adjustColumnWidths(hasAdditionalInfo);
    }

    protected TableColumn<T, String> getAdditionalInfoColumn() {
        TableColumn<T, String> additionalInfoColumn = new TableColumn<>(Constants.ADDITIONAL_INFO_LABEL);
        additionalInfoColumn.setCellValueFactory(cellData -> {
            try {
                Method method = cellData.getValue().getClass().getMethod("additionalInfoProperty");
                return (ObservableValue<String>) method.invoke(cellData.getValue());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return new SimpleStringProperty("");
            }
        });
        additionalInfoColumn.setCellFactory(tc -> WrappedTableCellFactory.createWrappedCell());
        return additionalInfoColumn;
    }

    protected abstract void adjustColumnWidths(boolean hasAdditionalInfo);
}
