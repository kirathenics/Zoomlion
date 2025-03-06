package org.example.zoomlion.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.print.*;
import javafx.scene.control.TableView;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.List;

public class TablePrinter {
    // TODO: как не менять масштаб таблицы, так как пользователь видит изменения размеров таблицы, а иначе уместить таблицу на листе на печать
    public static <T> void print(TableView<T> tableView) {
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null && job.showPrintDialog(tableView.getScene().getWindow())) {
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
}
