package org.example.zoomlion.DB;

import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceDAO {
//    public static List<Integer> getMileageListByTechnicId(int technicId) {
//        List<Integer> mileageList = new ArrayList<>();
//
//        String query = """
//            SELECT M.mileage, M.is_periodic
//            FROM maintenance
//            WHERE technic_id = ?
//                AND mileage IS NOT NULL
//            GROUP BY M.mileage, M.is_periodic
//            ORDER BY M.mileage
//            """;
//
//        try (Connection connection = DatabaseConnector.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setInt(1, technicId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                mileageList.add(resultSet.getInt("mileage"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return mileageList;
//    }
//
//    public static List<Integer> getMileageLubricationListByTechnicId(int technicId) {
//        List<Integer> mileageList = new ArrayList<>();
//
//        String query = """
//            SELECT M.mileage, M.is_periodic
//            FROM lubrication_operations AS LO
//            LEFT JOIN maintenance AS M
//            ON M.id = LO.maintenance_id
//            WHERE M.technic_id = ?
//                AND M.mileage IS NOT NULL
//            GROUP BY M.mileage, M.is_periodic
//            ORDER BY M.mileage
//            """;
//
//        try (Connection connection = DatabaseConnector.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setInt(1, technicId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                mileageList.add(resultSet.getInt("mileage"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return mileageList;
//    }
//
//    public static List<Integer> getWorkHoursListByTechnicId(int technicId) {
//        List<Integer> workHoursList = new ArrayList<>();
//
//        String query = """
//            SELECT work_hours, is_periodic
//            FROM maintenance
//            WHERE technic_id = ?
//                AND work_hours IS NOT NULL
//            GROUP BY work_hours, is_periodic
//            ORDER BY work_hours
//            """;
//
//        try (Connection connection = DatabaseConnector.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setInt(1, technicId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                workHoursList.add(resultSet.getInt("work_hours"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return workHoursList;
//    }
//
//    public static List<Integer> getWorkHoursLubricationListByTechnicId(int technicId) {
//        List<Integer> workHoursList = new ArrayList<>();
//
//        String query = """
//            SELECT M.work_hours, M.is_periodic
//            FROM lubrication_operations AS LO
//            LEFT JOIN maintenance AS M
//            ON M.id = LO.maintenance_id
//            WHERE M.technic_id = ? AND M.work_hours IS NOT NULL
//            GROUP BY M.work_hours, M.is_periodic
//            ORDER BY M.work_hours
//            """;
//
//        try (Connection connection = DatabaseConnector.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setInt(1, technicId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                workHoursList.add(resultSet.getInt("work_hours"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return workHoursList;
//    }

    private static List<MaintenanceValue> getValueListByTechnicId(int technicId, String valueName) {
        List<MaintenanceValue> valueList = new ArrayList<>();

        String query = """
        SELECT %s, is_periodic
        FROM maintenance
        WHERE technic_id = ?
          AND %s IS NOT NULL
        GROUP BY %s, is_periodic
        ORDER BY %s ASC
        """.formatted(valueName, valueName, valueName, valueName);

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int value = resultSet.getInt(valueName);
                boolean isPeriodic = resultSet.getBoolean("is_periodic");
                Boolean isPeriodicNullable = resultSet.wasNull() ? null : isPeriodic;

                valueList.add(new MaintenanceValue(value, isPeriodicNullable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valueList;
    }

    public static List<MaintenanceValue> getMileageListByTechnicId(int technicId) {
        return getValueListByTechnicId(technicId, Constants.MILEAGE_VALUE);
    }

    public static List<MaintenanceValue> getWorkHoursListByTechnicId(int technicId) {
        return getValueListByTechnicId(technicId, Constants.WORK_HOURS_VALUE);
    }



    // создай общий private метод getValueListByTechnicID(int technicId, String valueName), потом вызывай его для getMileageListByTechnicId и getWorkHoursListByTechnicId передавая mileage и work_hours соответственно
    public static List<MaintenanceValue> getMileageListByTechnicId(int technicId) {
        List<MaintenanceValue> mileageList = new ArrayList<>();

        String query = """
            SELECT mileage, is_periodic
            FROM maintenance
            WHERE technic_id = ?
                AND mileage IS NOT NULL
            GROUP BY M.mileage, M.is_periodic
            ORDER BY M.mileage ASC
            """;

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int workHours = resultSet.getInt("mileage");
                boolean isPeriodic = resultSet.getBoolean("is_periodic");
                Boolean isPeriodicNullable = resultSet.wasNull() ? null : isPeriodic;

                mileageList.add(new MaintenanceValue(workHours, isPeriodicNullable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mileageList;
    }

    public static List<MaintenanceValue> getMileageLubricationListByTechnicId(int technicId) {
        List<MaintenanceValue> mileageList = new ArrayList<>();

        String query = """
            SELECT M.mileage, M.is_periodic
            FROM lubrication_operations AS LO
            LEFT JOIN maintenance AS M
            ON M.id = LO.maintenance_id
            WHERE M.technic_id = ?
                AND M.mileage IS NOT NULL
            GROUP BY M.mileage, M.is_periodic
            ORDER BY M.mileage ASC
            """;

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int workHours = resultSet.getInt("mileage");
                boolean isPeriodic = resultSet.getBoolean("is_periodic");
                Boolean isPeriodicNullable = resultSet.wasNull() ? null : isPeriodic;

                mileageList.add(new MaintenanceValue(workHours, isPeriodicNullable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mileageList;
    }

    public static List<MaintenanceValue> getWorkHoursListByTechnicId(int technicId) {
        List<MaintenanceValue> workHoursList = new ArrayList<>();

        String query = """
            SELECT work_hours, is_periodic
            FROM maintenance
            WHERE technic_id = ?
                AND work_hours IS NOT NULL
            GROUP BY work_hours, is_periodic
            ORDER BY work_hours ASC
            """;

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int workHours = resultSet.getInt("work_hours");
                boolean isPeriodic = resultSet.getBoolean("is_periodic");
                Boolean isPeriodicNullable = resultSet.wasNull() ? null : isPeriodic;

                workHoursList.add(new MaintenanceValue(workHours, isPeriodicNullable));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workHoursList;
    }

    public static List<MaintenanceValue> getWorkHoursLubricationListByTechnicId(int technicId) {
        List<MaintenanceValue> workHoursList = new ArrayList<>();

        String query = """
            SELECT M.work_hours, M.is_periodic
            FROM lubrication_operations AS LO
            LEFT JOIN maintenance AS M
            ON M.id = LO.maintenance_id
            WHERE M.technic_id = ? AND M.work_hours IS NOT NULL
            GROUP BY M.work_hours, M.is_periodic
            ORDER BY M.work_hours ASC
            """;

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, technicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int workHours = resultSet.getInt("work_hours");
                boolean isPeriodic = resultSet.getBoolean("is_periodic");
                Boolean isPeriodicNullable = resultSet.wasNull() ? null : isPeriodic;

                workHoursList.add(new MaintenanceValue(workHours, isPeriodicNullable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workHoursList;
    }

    public static List<MileageMaintenance> getMileageMaintenanceByTechnicId(int technicId, int mileage, Boolean isPeriodic) {
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
            ORDER BY M.mileage ASC
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

    public static List<MileageLubrication> getMileageLubricationByTechnicId(int technicId, int mileage, Boolean isPeriodic) {
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
            ORDER BY M.mileage ASC
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

    public static List<WorkHoursMaintenance> getWorkHoursMaintenanceByTechnicId(int technicId, int workHours, Boolean isPeriodic) {
        List<WorkHoursMaintenance> workHoursMaintenanceList = new ArrayList<>();

        String query = """
            SELECT
                MO.name AS maintenance_object,
                W.name AS work_content,
                M.work_hours AS work_hours,
                M.additional_info AS additional_info
            FROM maintenance AS M
            LEFT JOIN operations AS O ON O.id = M.operation_id
            LEFT JOIN maintenance_objects AS MO ON MO.id = O.maintenance_object_id
            LEFT JOIN work_contents AS W ON W.id = O.work_content_id
            WHERE M.technic_id = ?
                AND M.work_hours IS NOT NULL
        """;

        StringBuilder queryBuilder = new StringBuilder(query);

        if (isPeriodic == null) {
            queryBuilder.append(" AND M.is_periodic IS NULL");
            queryBuilder.append(" AND ? % M.work_hours = 0");
        } else {
            if (isPeriodic) {
                queryBuilder.append(" AND ? % M.work_hours = 0");
            }
            else {
                queryBuilder.append(" AND M.work_hours = ?");
            }
            queryBuilder.append(" AND M.is_periodic = ?");
        }

        queryBuilder.append(" ORDER BY M.work_hours ASC");

        query = queryBuilder.toString();

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int parameterIndex = 1;

            preparedStatement.setInt(parameterIndex++, technicId);
            preparedStatement.setInt(parameterIndex++, workHours);

            if (isPeriodic != null) {
                preparedStatement.setBoolean(parameterIndex, isPeriodic);
            }

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
    
    public static List<WorkHoursLubrication> getWorkHoursLubricationByTechnicId(int technicId, int workHours, Boolean isPeriodic) {
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
            ORDER BY M.work_hours ASC
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
