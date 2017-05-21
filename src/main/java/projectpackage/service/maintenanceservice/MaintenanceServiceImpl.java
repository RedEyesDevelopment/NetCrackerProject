package projectpackage.service.maintenanceservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.maintenancedao.MaintenanceDAO;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
@Log4j
@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    private static final Logger LOGGER = Logger.getLogger(MaintenanceServiceImpl.class);

    @Autowired
    MaintenanceDAO maintenanceDAO;

    @Override
    public List<Maintenance> getAllMaintenances() {
        List<Maintenance> maintenances = maintenanceDAO.getAllMaintenances();
        if (null == maintenances) LOGGER.info("Returned NULL!!!");
        return maintenances;
    }

    @Override
    public List<Maintenance> getAllMaintenances(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public Maintenance getSingleMaintenanceById(int id) {
        Maintenance maintenance = maintenanceDAO.getMaintenance(id);
        if (null == maintenance) LOGGER.info("Returned NULL!!!");
        return maintenance;
    }

    @Override
    public boolean deleteMaintenance(int id) {
        int count = maintenanceDAO.deleteMaintenance(id);
        LOGGER.info("Deleted rows : " + count);
        if (count == 0) return false;
        return true;
    }

    @Override
    public boolean insertMaintenance(Maintenance maintenance) {
        try {
            int maintenanceId = maintenanceDAO.insertMaintenance(maintenance);
            LOGGER.info("Get from DB maintenanceId = " + maintenanceId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateMaintenance(int id, Maintenance newMaintenance) {
        try {
            newMaintenance.setObjectId(id);
            Maintenance oldMaintenance = maintenanceDAO.getMaintenance(id);
            maintenanceDAO.updateMaintenance(newMaintenance, oldMaintenance);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
