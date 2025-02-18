package org.example.zoomlion.UIFactory;

public class TechnicUIFactoryProvider {
    public static TechnicUIFactory getFactory(String technicType) {
        return switch (technicType) {
            case "Стреловой самоходный кран" -> new CraneUIFactory();
            case "Подъемник" -> new ElevatorUIFactory();
            default -> new DefaultUIFactory();
        };
    }
}
