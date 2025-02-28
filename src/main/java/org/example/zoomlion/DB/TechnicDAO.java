package org.example.zoomlion.DB;

import org.example.zoomlion.models.Technic;
import org.example.zoomlion.models.TechnicType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechnicDAO {
    public static List<Technic> loadTechnic() {
        List<Technic> technicList = new ArrayList<>();

        String query = "SELECT * FROM technics";

        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

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

    public static List<TechnicType> loadTechnicTypes() {
        List<TechnicType> technicTypeList = new ArrayList<>();

        String query = "SELECT * FROM technic_types";

        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                technicTypeList.add(new TechnicType(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technicTypeList;
    }

    public static List<String> loadTechnicTypeNames() {
        List<String> technicTypeNameList = new ArrayList<>();

        String query = "SELECT name FROM technic_types";

        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                technicTypeNameList.add(
                        resultSet.getString("name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technicTypeNameList;
    }

    public static String getTechnicTypeByTechnicId(int technicId) {
        String query = """
            SELECT technic_types.name
            FROM technic_types
            LEFT JOIN technic_models
            ON technic_types.id = technic_models.technic_type_id
            LEFT JOIN technics
            ON technic_models.id = technics.technic_model_id
            WHERE technics.id = ?
        """;

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
