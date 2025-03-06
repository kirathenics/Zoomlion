package org.example.zoomlion.TableViewFactory;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.VBox;
import org.example.zoomlion.utils.Constants;
import org.example.zoomlion.utils.TablePrinter;

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

        observableList.addListener((ListChangeListener<T>) change -> Platform.runLater(this::adjustTableSize));

        setupTableSize();
        tableView.getStyleClass().add("maintenance-table-view");

        ContextMenu contextMenu = new ContextMenu();
        MenuItem printItem = new MenuItem(Constants.PRINT_LABEL);
        contextMenu.getItems().add(printItem);

        printItem.setOnAction(e -> {
            AbstractMaintenanceTable<T> tableCopy = cloneTable();
            VBox.setMargin(tableCopy.tableContainer, new Insets(500, 0, 0, 0));
            tableContainer.getChildren().add(tableCopy.getTableContainer());
            TablePrinter.print(tableCopy.tableView);
            tableContainer.getChildren().remove(tableCopy.getTableContainer());
        });

        tableView.setOnContextMenuRequested(event -> {
            contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
            event.consume();
        });

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

        tableView.applyCss();
        tableView.layout();

        TableViewSkin<?> skin = (TableViewSkin<?>) tableView.getSkin();
        if (skin != null) {
            VirtualFlow<?> flow = (VirtualFlow<?>) skin.getChildren().get(1);
            double totalHeight = tableView.lookup(".column-header-background").getBoundsInLocal().getHeight();

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

    public void updateTable(Supplier<List<T>> dataSupplier) {
        observableList.setAll(dataSupplier.get());

        setAdditionalInfoColumn();

        Platform.runLater(this::adjustTableSize);
        Platform.runLater(() -> Platform.runLater(this::adjustTableSize));
    }

    public void updateTable(List<T> items) {
        observableList.setAll(items);

        setAdditionalInfoColumn();

        Platform.runLater(this::adjustTableSize);
        Platform.runLater(() -> Platform.runLater(this::adjustTableSize));
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

        tableView.getColumns().stream()
                .filter(col -> Constants.ADDITIONAL_INFO_LABEL.equals(col.getText()))
                .findFirst().ifPresent(additionalInfoColumn -> additionalInfoColumn.setVisible(hasAdditionalInfo));

        adjustColumnWidths(hasAdditionalInfo);
    }

    protected abstract void adjustColumnWidths(boolean hasAdditionalInfo);

    protected abstract AbstractMaintenanceTable<T> cloneTable();
}
