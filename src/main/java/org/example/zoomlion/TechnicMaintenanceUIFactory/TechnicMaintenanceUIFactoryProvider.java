package org.example.zoomlion.TechnicMaintenanceUIFactory;

import org.example.zoomlion.Utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class TechnicMaintenanceUIFactoryProvider {
    private static final Map<String, TechnicMaintenanceUIFactory> FACTORY_MAP = new HashMap<>();

    static {
        FACTORY_MAP.put(Constants.CRANE_TECHNIC_TYPE, new CraneMaintenanceUIFactory());
        FACTORY_MAP.put(Constants.ELEVATOR_TECHNIC_TYPE, new ElevatorMaintenanceUIFactory());
        FACTORY_MAP.put(Constants.DEFAULT_TECHNIC_TYPE, new DefaultMaintenanceUIFactory());
    }

    public static TechnicMaintenanceUIFactory getFactory(String technicType) {
        return FACTORY_MAP.getOrDefault(technicType, new DefaultMaintenanceUIFactory());
    }
}
