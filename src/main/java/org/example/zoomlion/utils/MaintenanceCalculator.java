package org.example.zoomlion.utils;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceCalculator {
    public static Integer getNextMaintenance(int currentMileage, List<Integer> maintenanceIntervals) {
        List<Integer> nextMaintenance = new ArrayList<>();

        for (int interval : maintenanceIntervals) {
            int prevMileage = currentMileage - currentMileage % interval;
            nextMaintenance.add(prevMileage + interval);
        }

        int minDiff = Integer.MAX_VALUE;
        Integer nextServiceInterval = null;

        for (int i = 0; i < nextMaintenance.size(); i++) {
            int diff = nextMaintenance.get(i) - currentMileage;

            if (diff >= 0 && diff <= minDiff) {
                minDiff = diff;
                nextServiceInterval = maintenanceIntervals.get(i);
            }
        }

        return nextServiceInterval;
    }
}
