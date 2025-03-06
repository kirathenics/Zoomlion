package org.example.zoomlion.DB;

import org.example.zoomlion.DB.DatabaseConnectors.SqliteDatabaseConnector;
import org.example.zoomlion.utils.Constants;
import org.example.zoomlion.models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceDAO {
    public static List<MaintenanceValue> getMileageListByTechnicId(int technicId) {
        return getValueListByTechnicId(technicId, Constants.MILEAGE_VALUE);
    }

    public static List<MaintenanceValue> getWorkHoursListByTechnicId(int technicId) {
        return getValueListByTechnicId(technicId, Constants.WORK_HOURS_VALUE);
    }

    private static List<MaintenanceValue> getValueListByTechnicId(int technicId, String valueName) {
        List<MaintenanceValue> valueList = new ArrayList<>();

        String query = String.format("""
            SELECT %s, is_periodic
            FROM maintenance
            WHERE technic_id = ?
              AND %s IS NOT NULL
            GROUP BY %s, is_periodic
            ORDER BY %s ASC
        """, valueName, valueName, valueName, valueName);

        try (Connection connection = SqliteDatabaseConnector.getConnection();
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

    public static List<MaintenanceValue> getMileageLubricationListByTechnicId(int technicId) {
        return getValueLubricationListByTechnicId(technicId, Constants.MILEAGE_VALUE);
    }

    public static List<MaintenanceValue> getWorkHoursLubricationListByTechnicId(int technicId) {
        return getValueLubricationListByTechnicId(technicId, Constants.WORK_HOURS_VALUE);
    }

    private static List<MaintenanceValue> getValueLubricationListByTechnicId(int technicId, String valueName) {
        List<MaintenanceValue> valueList = new ArrayList<>();

        String query = String.format("""
            SELECT M.%s, M.is_periodic
            FROM lubrication_operations AS LO
            LEFT JOIN maintenance AS M
            ON M.id = LO.maintenance_id
            WHERE M.technic_id = ?
                AND M.%s IS NOT NULL
            GROUP BY M.%s, M.is_periodic
            ORDER BY M.%s ASC
        """, valueName, valueName, valueName, valueName);

        try (Connection connection = SqliteDatabaseConnector.getConnection();
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

    public static List<AbstractValueMaintenance> getMileageMaintenanceByTechnicId(int technicId, int mileage, Boolean isPeriodic) {
        return getValueMaintenanceByTechnicId(technicId, mileage, isPeriodic, Constants.MILEAGE_VALUE);
    }

    public static List<AbstractValueMaintenance> getWorkHoursMaintenanceByTechnicId(int technicId, int mileage, Boolean isPeriodic) {
        return getValueMaintenanceByTechnicId(technicId, mileage, isPeriodic, Constants.WORK_HOURS_VALUE);
    }

    private static List<AbstractValueMaintenance> getValueMaintenanceByTechnicId(int technicId, int value, Boolean isPeriodic, String valueName) {
        List<AbstractValueMaintenance> valueMaintenanceList = new ArrayList<>();

        String query = String.format("""
            SELECT
                MO.name AS maintenance_object,
                W.name AS work_content,
                M.%s AS %s,
                M.additional_info AS additional_info
            FROM maintenance AS M
            LEFT JOIN operations AS O ON O.id = M.operation_id
            LEFT JOIN maintenance_objects AS MO ON MO.id = O.maintenance_object_id
            LEFT JOIN work_contents AS W ON W.id = O.work_content_id
            WHERE M.technic_id = ?
                AND M.%s IS NOT NULL
        """, valueName, valueName, valueName);

        StringBuilder queryBuilder = new StringBuilder(query);

        if (isPeriodic == null) {
            queryBuilder.append(String.format(" AND M.is_periodic IS NULL AND ? %% M.%s = 0", valueName));
        } else {
            if (isPeriodic) {
                queryBuilder.append(String.format(" AND ? %% M.%s = 0", valueName));
            } else {
                queryBuilder.append(String.format(" AND M.%s = ?", valueName));
            }
            queryBuilder.append(" AND M.is_periodic = ?");
        }

        queryBuilder.append(String.format(" ORDER BY M.%s ASC", valueName));

        query = queryBuilder.toString();

        try (Connection connection = SqliteDatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int parameterIndex = 1;

            preparedStatement.setInt(parameterIndex++, technicId);
            preparedStatement.setInt(parameterIndex++, value);
            if (isPeriodic != null) {
                preparedStatement.setBoolean(parameterIndex, isPeriodic);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                valueMaintenanceList.add(new AbstractValueMaintenance(
                        resultSet.getString("maintenance_object"),
                        resultSet.getString("work_content"),
                        resultSet.getInt(valueName),
                        resultSet.getString("additional_info")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valueMaintenanceList;
    }

    public static List<AbstractValueLubrication> getMileageLubricationByTechnicId(int technicId, int mileage, Boolean isPeriodic) {
        return getValueLubricationByTechnicId(technicId, mileage, isPeriodic, Constants.MILEAGE_VALUE);
    }

    public static List<AbstractValueLubrication> getWorkHoursLubricationByTechnicId(int technicId, int mileage, Boolean isPeriodic) {
        return getValueLubricationByTechnicId(technicId, mileage, isPeriodic, Constants.WORK_HOURS_VALUE);
    }

        private static List<AbstractValueLubrication> getValueLubricationByTechnicId(int technicId, int value, Boolean isPeriodic, String valueName) {
        List<AbstractValueLubrication> valueLubricationList = new ArrayList<>();

        String query = String.format("""
            SELECT
                LP.name AS lubrication_point,
                LM.name AS lubrication_method,
                M.%s AS %s,
                L.name AS lubricant,
                M.additional_info AS additional_info
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
                AND M.%s IS NOT NULL
        """, valueName, valueName, valueName);

        StringBuilder queryBuilder = new StringBuilder(query);

        if (isPeriodic == null) {
            queryBuilder.append(String.format(" AND M.is_periodic IS NULL AND ? %% M.%s = 0", valueName));
        } else {
            if (isPeriodic) {
                queryBuilder.append(String.format(" AND ? %% M.%s = 0", valueName));
            } else {
                queryBuilder.append(String.format(" AND M.%s = ?", valueName));
            }
            queryBuilder.append(" AND M.is_periodic = ?");
        }

        queryBuilder.append(" ORDER BY M.%s ASC".formatted(valueName));

        query = queryBuilder.toString();

        try (Connection connection = SqliteDatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int parameterIndex = 1;

            preparedStatement.setInt(parameterIndex++, technicId);
            preparedStatement.setInt(parameterIndex++, value);
            if (isPeriodic != null) {
                preparedStatement.setBoolean(parameterIndex, isPeriodic);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                valueLubricationList.add(new AbstractValueLubrication(
                        resultSet.getString("lubrication_point"),
                        resultSet.getString("lubrication_method"),
                        resultSet.getInt(valueName),
                        resultSet.getString("lubricant"),
                        resultSet.getString("additional_info")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valueLubricationList;
    }
}
