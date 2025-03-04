package org.example.zoomlion.Utils.ProjectUpdate;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class GitHubUpdater {
    public static void downloadUpdate(String fileURL, String savePath) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileURL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }

            System.out.println("Update downloaded successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
