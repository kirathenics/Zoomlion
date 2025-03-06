package org.example.zoomlion.MaintenanceUI;

import org.example.zoomlion.DB.MaintenanceDAO;
import org.example.zoomlion.utils.Constants;
import org.example.zoomlion.models.*;

import java.util.List;

public class MileageMaintenanceUI extends AbstractMaintenanceUI<AbstractValueMaintenance, AbstractValueLubrication> {
    public MileageMaintenanceUI(Technic technic) {
        super(technic, Constants.MILEAGE_TO_LABEL, Constants.MILEAGE_LABEL, Constants.LUBRICATION_MILEAGE_LABEL,"mileage");
    }

    @Override
    protected List<MaintenanceValue> getMaintenanceList() {
        return MaintenanceDAO.getMileageListByTechnicId(technic.getId());
    }

    @Override
    protected List<MaintenanceValue> getLubricationList() {
        return MaintenanceDAO.getMileageLubricationListByTechnicId(technic.getId());
    }

    @Override
    protected List<AbstractValueMaintenance> fetchMaintenanceData(int value, Boolean isPeriodic) {
        return MaintenanceDAO.getMileageMaintenanceByTechnicId(technic.getId(), value, isPeriodic);
    }

    @Override
    protected List<AbstractValueLubrication> fetchLubricationData(int value, Boolean isPeriodic) {
        return MaintenanceDAO.getMileageLubricationByTechnicId(technic.getId(), value, isPeriodic);
    }
}