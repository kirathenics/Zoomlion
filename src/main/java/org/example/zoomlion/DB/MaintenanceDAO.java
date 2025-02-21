package org.example.zoomlion.DB;

import org.example.zoomlion.models.MileageLubrication;
import org.example.zoomlion.models.MileageMaintenance;
import org.example.zoomlion.models.WorkHoursLubrication;
import org.example.zoomlion.models.WorkHoursMaintenance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceDAO {
    public static List<Integer> getMileageListByTechnicId(int technicId) {
        List<Integer> mileageList = new ArrayList<>();

        String query = """
            SELECT mileage
            FROM maintenance
            WHERE technic_id = ?
                AND mileage IS NOT NULL
            GROUP BY mileage
            """;

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

    public static List<Integer> getMileageLubricationListByTechnicId(int technicId) {
        List<Integer> mileageList = new ArrayList<>();

        String query = """
            SELECT M.mileage
            FROM lubrication_operations AS LO
            LEFT JOIN maintenance AS M
            ON M.id = LO.maintenance_id
            WHERE M.technic_id = ?
                AND M.mileage IS NOT NULL
            GROUP BY M.mileage
            """;

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

    public static List<Integer> getWorkHoursListByTechnicId(int technicId) {
        List<Integer> workHoursList = new ArrayList<>();

        String query = """
            SELECT work_hours
            FROM maintenance
            WHERE technic_id = ?
                AND work_hours IS NOT NULL
            GROUP BY work_hours
            """;

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

    public static List<Integer> getWorkHoursLubricationListByTechnicId(int technicId) {
        List<Integer> workHoursList = new ArrayList<>();

        String query = """
            SELECT M.work_hours
            FROM lubrication_operations AS LO
            LEFT JOIN maintenance AS M
            ON M.id = LO.maintenance_id
            WHERE M.technic_id = ? AND M.work_hours IS NOT NULL
            GROUP BY M.work_hours
            """;

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

    public static List<MileageMaintenance> getMileageMaintenanceByTechnicId(int technicId, int mileage) {
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
            WHERE M.technic_id = ?
                AND M.mileage IS NOT NULL
                AND ? % M.mileage = 0
            """;

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            preparedStatement.setInt(2, mileage);
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

    public static List<MileageLubrication> getMileageLubricationByTechnicId(int technicId, int mileage) {
        List<MileageLubrication> mileageLubricationList = new ArrayList<>();

        String query = """
            SELECT
                LP.name AS 'lubrication_point',
                LM.name AS 'lubrication_method',
                M.mileage AS 'mileage',
                L.name AS 'lubricant',
                M.additional_info AS 'additional_info'
            FROM lubrication_operations AS LO
            LEFT JOIN maintenance AS M
            ON M.id = LO.maintenance_id
            LEFT JOIN lubrication_points AS LP
            ON LP.id = LO.lubrication_point_id
            LEFT JOIN lubrication_methods AS LM
            ON LM.id = LO.lubrication_method_id
            LEFT JOIN lubricants AS L
            ON L.id = LO.lubricant_id
            WHERE M.technic_id = ?
                AND M.mileage IS NOT NULL
                AND ? % M.mileage = 0
            """;

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            preparedStatement.setInt(2, mileage);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mileageLubricationList.add(new MileageLubrication(
                        resultSet.getString("lubrication_point"),
                        resultSet.getString("lubrication_method"),
                        resultSet.getInt("mileage"),
                        resultSet.getString("lubricant"),
                        resultSet.getNString("additional_info")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mileageLubricationList;
    }

    public static List<WorkHoursMaintenance> getWorkHoursMaintenanceByTechnicId(int technicId, int workHours) {
        List<WorkHoursMaintenance> workHoursMaintenanceList = new ArrayList<>();

        String query = """
            SELECT
                MO.name AS 'maintenance_object',
                W.name AS 'work_content',
                M.work_hours AS 'work_hours',
                M.additional_info AS 'additional_info'
            FROM maintenance as M
            LEFT JOIN operations AS O
            ON O.id = M.operation_id
            LEFT JOIN maintenance_objects AS MO
            ON MO.id = O.maintenance_object_id
            LEFT JOIN work_contents AS W
            ON W.id = O.work_content_id
            WHERE M.technic_id = ?
                AND M.work_hours IS NOT NULL
                AND ? % M.work_hours = 0
            """;

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            preparedStatement.setInt(2, workHours);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                workHoursMaintenanceList.add(new WorkHoursMaintenance(
                        resultSet.getString("maintenance_object"),
                        resultSet.getString("work_content"),
                        resultSet.getInt("work_hours"),
                        resultSet.getNString("additional_info")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workHoursMaintenanceList;
    }

    public static List<WorkHoursLubrication> getWorkHoursLubricationByTechnicId(int technicId, int workHours) {
        List<WorkHoursLubrication> workHoursLubricationList = new ArrayList<>();

        String query = """
            SELECT
                LP.name AS 'lubrication_point',
                LM.name AS 'lubrication_method',
                M.work_hours AS 'work_hours',
                L.name AS 'lubricant',
                M.additional_info AS 'additional_info'
            FROM lubrication_operations AS LO
            LEFT JOIN maintenance AS M
            ON M.id = LO.maintenance_id
            LEFT JOIN lubrication_points AS LP
            ON LP.id = LO.lubrication_point_id
            LEFT JOIN lubrication_methods AS LM
            ON LM.id = LO.lubrication_method_id
            LEFT JOIN lubricants AS L
            ON L.id = LO.lubricant_id
            WHERE M.technic_id = ?
                AND M.work_hours IS NOT NULL
                AND ? % M.work_hours = 0
            """;

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            preparedStatement.setInt(2, workHours);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                workHoursLubricationList.add(new WorkHoursLubrication(
                        resultSet.getString("lubrication_point"),
                        resultSet.getString("lubrication_method"),
                        resultSet.getInt("work_hours"),
                        resultSet.getString("lubricant"),
                        resultSet.getNString("additional_info")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workHoursLubricationList;
    }
}
