package org.example.zoomlion.DB;

import org.example.zoomlion.models.MileageMaintenance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceDAO {
    public static List<Integer> getWorkHoursByTechnicId(int technicId) {
        List<Integer> workHoursList = new ArrayList<>();

        String query = "SELECT work_hours FROM maintenance WHERE technic_id = ? AND work_hours IS NOT NULL GROUP BY work_hours";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                workHoursList.add(resultSet.getInt("work_hours"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workHoursList;
    }

    public static List<Integer> getMileageByTechnicId(int technicId) {
        List<Integer> mileageList = new ArrayList<>();

        String query = "SELECT mileage FROM maintenance WHERE technic_id = ? AND mileage IS NOT NULL GROUP BY mileage";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mileageList.add(resultSet.getInt("mileage"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mileageList;
    }

    public static List<MileageMaintenance> loadMileageMaintenanceByTechnicId(int technicId) {
        List<MileageMaintenance> mileageMaintenanceList = new ArrayList<>();

        String query = """
            SELECT
                MO.name AS 'maintenance_object',
                W.name AS 'work_content',
                M.mileage AS 'mileage',
                M.additional_info AS 'additional_info'
            FROM maintenance as M
            LEFT JOIN operations AS O
            ON O.id = M.operation_id
            LEFT JOIN maintenance_objects AS MO
            ON MO.id = O.maintenance_object_id
            LEFT JOIN work_contents AS W
            ON W.id = O.work_content_id
            WHERE M.technic_id = ? AND M.mileage IS NOT NULL
            """;

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mileageMaintenanceList.add(new MileageMaintenance(
                    resultSet.getString("maintenance_object"),
                    resultSet.getString("work_content"),
                    resultSet.getInt("mileage"),
                    resultSet.getNString("additional_info")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mileageMaintenanceList;
    }
}
