package org.example.zoomlion.Utils.ProjectUpdate;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class GitHubUpdater {
    public static void downloadUpdate(String fileURL, String savePath, ProgressBar progressBar) {
        new Thread(() -> {
            try (BufferedInputStream in = new BufferedInputStream(new URL(fileURL).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {

                URL url = new URL(fileURL);
                int fileSize = url.openConnection().getContentLength();
                byte[] dataBuffer = new byte[1024];
                int bytesRead, downloaded = 0;

                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                    downloaded += bytesRead;
                    double progress = (double) downloaded / fileSize;

                    Platform.runLater(() -> progressBar.setProgress(progress));
                }

                System.out.println("Update downloaded successfully!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
