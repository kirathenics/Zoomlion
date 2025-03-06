package org.example.zoomlion.utils.ProjectUpdate;

import java.io.File;

public class AppRestarter {
    public static void restartApplication(String newJarPath) {
        try {
            String javaBin = System.getProperty("java.home") + "/bin/java";
            File currentJar = new File(AppRestarter.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            ProcessBuilder builder = new ProcessBuilder(javaBin, "-jar", newJarPath);
            builder.start();
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

