package org.example.zoomlion.MaintenanceUI;

import org.example.zoomlion.DB.MaintenanceDAO;
import org.example.zoomlion.Utils.Constants;
import org.example.zoomlion.models.MileageLubrication;
import org.example.zoomlion.models.MileageMaintenance;
import org.example.zoomlion.models.Technic;

import java.util.List;

public class MileageMaintenanceUI extends AbstractMaintenanceUI<MileageMaintenance, MileageLubrication> {
    public MileageMaintenanceUI(Technic technic) {
        super(technic, Constants.MILEAGE_TO_LABEL, Constants.MILEAGE_LABEL, "mileage");
    }

    @Override
    protected List<Integer> getMaintenanceList() {
        return MaintenanceDAO.getMileageListByTechnicId(technic.getId());
    }

    @Override
    protected List<Integer> getLubricationList() {
        return MaintenanceDAO.getMileageLubricationListByTechnicId(technic.getId());
    }

    @Override
    protected List<MileageMaintenance> fetchMaintenanceData(int value) {
        return MaintenanceDAO.getMileageMaintenanceByTechnicId(technic.getId(), value);
    }

    @Override
    protected List<MileageLubrication> fetchLubricationData(int value) {
        return MaintenanceDAO.getMileageLubricationByTechnicId(technic.getId(), value);
    }
}