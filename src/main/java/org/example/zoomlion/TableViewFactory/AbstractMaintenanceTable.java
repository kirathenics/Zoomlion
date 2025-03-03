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

public abstract class AbstractMaintenanceTable<T> implements Cloneable {
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

//        tableView.setOnContextMenuRequested(event -> {
//            System.out.println("ПКМ по таблице");
//            event.consume();
//        });

        // TODO: пофиксить бесконечное создание contextMenu
        tableView.setOnContextMenuRequested(event -> {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem printItem = new MenuItem("Печать");

            printItem.setOnAction(e -> {
                AbstractMaintenanceTable<T> abstractMaintenanceTable = this.clone();
                printTable(abstractMaintenanceTable.tableView);
            });

            contextMenu.getItems().add(printItem);
            contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
            event.consume();
        });

        tableContainer.getChildren().add(tableView);
    }

//    public void printTable(TableView<T> root) {
//        PrinterJob job = PrinterJob.createPrinterJob();
//
//        if (job != null && job.showPrintDialog(root.getScene().getWindow())) {
//            Printer printer = job.getPrinter();
//            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.EQUAL);
//
//            // Получаем фактические размеры содержимого, которое нужно распечатать
//            double contentWidth = root.getWidth();
//            double contentHeight = root.getHeight();
//
//            System.out.println("root.getWidth() " + root.getWidth());
//            System.out.println("root.getHeight() " + root.getHeight());
//
//            // Получаем разрешение печати
//            PrintResolution resolution = job.getJobSettings().getPrintResolution();
//
//            // Преобразуем размеры в дюймы с учетом разрешения
////            contentWidth /= resolution.getFeedResolution();
////            contentHeight /= resolution.getCrossFeedResolution();
//
//            System.out.println("resolution.getFeedResolution() " + resolution.getFeedResolution());
//            System.out.println("resolution.getCrossFeedResolution() " + resolution.getCrossFeedResolution());
//
//            System.out.println("pageLayout.getPrintableWidth() " + pageLayout.getPrintableWidth());
//            System.out.println("pageLayout.getPrintableHeight() " + pageLayout.getPrintableHeight());
//
//            // Рассчитываем масштабы для обеих осей
//            double scaleX = pageLayout.getPrintableWidth() / contentWidth;
////            double scaleY = pageLayout.getPrintableHeight() / contentHeight;
//
//            System.out.println("scaleX " + scaleX);
////            System.out.println("scaleY " + scaleY);
//
//            // Выбираем наименьший масштаб, чтобы сохранить пропорции
////            double scale = Math.min(scaleX, scaleY);
//
//            // Применяем масштабирование
//            Scale scaleTransform = new Scale(scaleX, scaleX);
//            root.getTransforms().add(scaleTransform);
//
//            // Печатаем страницу
//            boolean success = job.printPage(pageLayout, root);
//            if (success) {
//                job.endJob();
//            }
//
//            // Убираем трансформации после печати
//            root.getTransforms().remove(scaleTransform);
//        }
//    }

//    public void printTable(TableView<T> root) {
//        PrinterJob job = PrinterJob.createPrinterJob();
//
//        if (job != null && job.showPrintDialog(root.getScene().getWindow())) {
//            Printer printer = job.getPrinter();
//            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.EQUAL);
//
//            double contentWidth = root.getWidth();
//            double contentHeight = root.getHeight();
//
//            System.out.println("root.getWidth() " + root.getWidth());
//            System.out.println("root.getHeight() " + root.getHeight());
//
//            double scaleX = pageLayout.getPrintableWidth() / contentWidth;
//
//            double newHeight = contentHeight * scaleX;
//
//            Scale scaleTransform = new Scale(scaleX, scaleX);
//            root.getTransforms().add(scaleTransform);
//
//            boolean success = job.printPage(pageLayout, root);
//            if (success) {
//                job.endJob();
//            }
//
//            root.getTransforms().remove(scaleTransform);
//        }
//    }

    public void printTable(TableView<T> tableView) {
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null && job.showPrintDialog(tableView.getScene().getWindow())) {
            Printer printer = job.getPrinter();
            PageLayout pageLayout = printer.createPageLayout(
                    Paper.A4, PageOrientation.PORTRAIT,
                    20, 20, 30, 30 // Левые, правые, верхние и нижние отступы (в миллиметрах)
            );


            double contentWidth = tableView.getWidth();
            double contentHeight = tableView.getHeight();

            double scaleX = pageLayout.getPrintableWidth() / contentWidth;
            double newHeight = contentHeight * scaleX;

            double maxPrintableHeight = pageLayout.getPrintableHeight();

            // Если таблица умещается на одну страницу, печатаем сразу
            if (newHeight <= maxPrintableHeight) {
                Scale scaleTransform = new Scale(scaleX, scaleX);
                tableView.getTransforms().add(scaleTransform);

                boolean success = job.printPage(pageLayout, tableView);
                if (success) {
                    job.endJob();
                }

                tableView.getTransforms().remove(scaleTransform);
            } else {
                // Разбиение на страницы
                ObservableList<T> originalItems = FXCollections.observableArrayList(tableView.getItems());
                int totalRows = originalItems.size();
                int visibleRowsPerPage = (int) Math.floor(totalRows * (maxPrintableHeight / newHeight));

                List<List<T>> pages = new ArrayList<>();
                for (int i = 0; i < totalRows; i += visibleRowsPerPage) {
                    pages.add(originalItems.subList(i, Math.min(i + visibleRowsPerPage, totalRows)));
                }

                // Печать постранично
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

                tableView.setItems(originalItems); // Восстанавливаем полные данные после печати
                job.endJob();
            }
        }
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

    /**
     * Обновление таблицы при выборе ТО
     */
    public void updateTable(Supplier<List<T>> dataSupplier) {
        observableList.setAll(dataSupplier.get());

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

    @Override
    public AbstractMaintenanceTable<T> clone() {
        try {
            AbstractMaintenanceTable clone = (AbstractMaintenanceTable) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
