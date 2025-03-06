package org.example.zoomlion.TableViewFactory;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.print.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import org.example.zoomlion.Utils.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
            printTable(tableCopy.tableView);
            tableContainer.getChildren().remove(tableCopy.getTableContainer());
        });

        tableView.setOnContextMenuRequested(event -> {
            contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
            event.consume();
        });

        tableContainer.getChildren().add(tableView);
    }

    // TODO: как не менять масштаб таблицы, так как пользователь видит изменения размеров таблицы, а иначе уместить таблицу на листе на печать
    public void printTable(TableView<T> tableView) {
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null && job.showPrintDialog(this.tableView.getScene().getWindow())) {
            Printer printer = job.getPrinter();
            PageLayout pageLayout = printer.createPageLayout(
                    Paper.A4, PageOrientation.PORTRAIT,
                    20, 20, 30, 30 // Левые, правые, верхние и нижние отступы (в миллиметрах)
            );

            double contentWidth = tableView.getWidth();
            double contentHeight = tableView.getHeight();

//            System.out.println(tableView.getWidth());
//            System.out.println(tableView.getHeight());

            double scaleX = pageLayout.getPrintableWidth() / contentWidth;
            double newHeight = contentHeight * scaleX;

            double maxPrintableHeight = pageLayout.getPrintableHeight();

            if (newHeight <= maxPrintableHeight) {
                Scale scaleTransform = new Scale(scaleX, scaleX);
                tableView.getTransforms().add(scaleTransform);

                boolean success = job.printPage(pageLayout, tableView);
                if (success) {
                    job.endJob();
                }

                tableView.getTransforms().remove(scaleTransform);
            } else {
                ObservableList<T> originalItems = FXCollections.observableArrayList(tableView.getItems());
                int totalRows = originalItems.size();
                int visibleRowsPerPage = (int) Math.floor(totalRows * (maxPrintableHeight / newHeight));
                visibleRowsPerPage -= 2;

                List<List<T>> pages = new ArrayList<>();
                for (int i = 0; i < totalRows; i += visibleRowsPerPage) {
                    pages.add(originalItems.subList(i, Math.min(i + visibleRowsPerPage, totalRows)));
                }

                for (List<T> pageItems : pages) {
                    tableView.setItems(FXCollections.observableArrayList(pageItems));

                    Scale scaleTransform = new Scale(scaleX, scaleX);
                    tableView.getTransforms().add(scaleTransform);

                    boolean success = job.printPage(pageLayout, tableView);
                    tableView.getTransforms().remove(scaleTransform);

                    if (!success) {
                        break;
                    }
                }

                tableView.setItems(originalItems);
                job.endJob();
            }
        }
    }

    protected abstract AbstractMaintenanceTable<T> cloneTable();

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
}
