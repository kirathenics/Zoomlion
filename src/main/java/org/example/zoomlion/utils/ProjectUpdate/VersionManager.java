package org.example.zoomlion.utils.ProjectUpdate;

import org.example.zoomlion.utils.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VersionManager {
    private static final String VERSION_FILE = Constants.PROJECT_PATH + "version.txt";

    public static String getCurrentVersion() {
        try {
            return new String(Files.readAllBytes(Paths.get(VERSION_FILE))).trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "0.0.0"; // Если файла нет, считаем версию 0.0.0
        }
    }

    public static void updateVersion(String newVersion) {
        try {
            Files.write(Paths.get(VERSION_FILE), newVersion.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
