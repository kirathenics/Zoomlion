package org.example.zoomlion.TableViewFactory;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.example.zoomlion.TableColumnFactory.TableWrappedCellFactory;
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

        tableView.setPrefHeight(450);
        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());

        tableView.prefWidthProperty().bind(tableContainer.widthProperty());
        tableView.setMaxWidth(Double.MAX_VALUE);
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

        tableView.getColumns().removeIf(col -> col.getText().equals(Constants.ADDITIONAL_INFO_LABEL));

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
        additionalInfoColumn.setCellFactory(tc -> TableWrappedCellFactory.createWrappedCell());
        return additionalInfoColumn;
    }

    protected abstract void adjustColumnWidths(boolean hasAdditionalInfo);
}

//        tableView.setFixedCellSize(30);
//        tableView.prefHeightProperty().bind(
//                Bindings.when(Bindings.isEmpty(tableView.getItems()))
//                        .then(0)
//                        .otherwise(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(30))
//        );

//        tableView.prefHeightProperty().bind(
//                Bindings.createDoubleBinding(() -> {
//                    int rowCount = tableView.getItems().size();
//                    double rowHeight = 30; // Примерная высота одной строки
//                    double headerHeight = 40; // Высота заголовка
//                    return rowCount > 0 ? (rowCount * rowHeight + headerHeight) : headerHeight;
//                }, tableView.getItems())
//        );