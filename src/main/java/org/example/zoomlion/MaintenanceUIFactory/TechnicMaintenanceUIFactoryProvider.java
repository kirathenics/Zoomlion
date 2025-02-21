package org.example.zoomlion.MaintenanceUIFactory;

public class TechnicMaintenanceUIFactoryProvider {
    public static TechnicMaintenanceUIFactory getFactory(String technicType) {
        return switch (technicType) {
            case "Стреловой самоходный кран" -> new CraneMaintenanceUIFactory();
            case "Подъемник" -> new ElevatorMaintenanceUIFactory();
            default -> new DefaultMaintenanceUIFactory();
        };
    }
}
