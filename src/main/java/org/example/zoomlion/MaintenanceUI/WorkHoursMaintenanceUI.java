package org.example.zoomlion.MaintenanceUI;

import org.example.zoomlion.DB.MaintenanceDAO;
import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.models.MaintenanceValue;
import org.example.zoomlion.models.Technic;
import org.example.zoomlion.models.WorkHoursLubrication;
import org.example.zoomlion.models.WorkHoursMaintenance;

import java.util.List;

public class WorkHoursMaintenanceUI extends AbstractMaintenanceUI<WorkHoursMaintenance, WorkHoursLubrication> {
    public WorkHoursMaintenanceUI(Technic technic) {
        super(technic, Constants.WORK_HOURS_TO_LABEL, Constants.WORK_HOURS_LABEL, Constants.LUBRICATION_WORK_HOURS_LABEL,"workHours");
    }

    @Override
    protected List<MaintenanceValue> getMaintenanceList() {
        return MaintenanceDAO.getWorkHoursListByTechnicId(technic.getId());
    }

    @Override
    protected List<MaintenanceValue> getLubricationList() {
        return MaintenanceDAO.getWorkHoursLubricationListByTechnicId(technic.getId());
    }

    @Override
    protected List<WorkHoursMaintenance> fetchMaintenanceData(int value, Boolean isPeriodic) {
        return MaintenanceDAO.getWorkHoursMaintenanceByTechnicId(technic.getId(), value, isPeriodic);
    }

    @Override
    protected List<WorkHoursLubrication> fetchLubricationData(int value, Boolean isPeriodic) {
        return MaintenanceDAO.getWorkHoursLubricationByTechnicId(technic.getId(), value, isPeriodic);
    }
}
