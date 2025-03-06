package org.example.zoomlion.utils.ProjectUpdate;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubUpdateChecker {
    private static final String API_URL = "https://api.github.com/repos/kirathenics/Zoomlion/releases/latest";
//    private static final String CURRENT_VERSION = "1.0.0";

    public static String checkForUpdates() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(response.toString());
            String latestVersion = json.getString("tag_name");
            String downloadUrl = json.getJSONArray("assets").getJSONObject(0).getString("browser_download_url");

            String currentVersion = VersionManager.getCurrentVersion();
            System.out.println(currentVersion);
//            String currentVersion = "1.0.0";
            if (!latestVersion.replace("v", "").equals(currentVersion)) {
                return downloadUrl;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
