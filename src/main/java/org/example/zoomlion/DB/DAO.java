package org.example.zoomlion.DB;

import org.example.zoomlion.models.Technic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    public static List<Technic> loadTechnic() {
        List<Technic> technicList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM technics")) {

            while (resultSet.next()) {
                technicList.add(new Technic(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("image_path"),
                        resultSet.getInt("technic_model_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technicList;
    }
}
