package projectpackage.service.maintenanceservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.maintenancedao.MaintenanceDAO;

import java.util.List;

@Log4j
@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    private static final Logger LOGGER = Logger.getLogger(MaintenanceServiceImpl.class);

    @Autowired
    MaintenanceDAO maintenanceDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Maintenance> getAllMaintenances() {
        return maintenanceDAO.getAllMaintenances();
    }

    @Transactional(readOnly = true)
    @Override
    public Maintenance getSingleMaintenanceById(Integer id) {
        return maintenanceDAO.getMaintenance(id);
    }

    @Transactional
    @Override
    public IUDAnswer deleteMaintenance(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        maintenanceDAO.deleteMaintenance(id);

        return new IUDAnswer(id, true);
    }

    @Transactional
    @Override
    public IUDAnswer insertMaintenance(Maintenance maintenance) {
        if (maintenance == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }

        Integer maintenanceId = maintenanceDAO.insertMaintenance(maintenance);

        return new IUDAnswer(maintenanceId,true);
    }

    @Transactional
    @Override
    public IUDAnswer updateMaintenance(Integer id, Maintenance newMaintenance) {
        if (newMaintenance == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        newMaintenance.setObjectId(id);
        Maintenance oldMaintenance = maintenanceDAO.getMaintenance(id);
        maintenanceDAO.updateMaintenance(newMaintenance, oldMaintenance);

        return new IUDAnswer(id,true);
    }
}
