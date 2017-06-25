package projectpackage.service.maintenanceservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.maintenancedao.MaintenanceDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

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
    public Maintenance getSingleMaintenanceById(Integer id) {
        Maintenance maintenance = maintenanceDAO.getMaintenance(id);
        if (null == maintenance) LOGGER.info("Returned NULL!!!");
        return maintenance;
    }

    @Override
    public IUDAnswer deleteMaintenance(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            maintenanceDAO.deleteMaintenance(id);
        } catch (ReferenceBreakException e) {
            return maintenanceDAO.rollback(id, ON_ENTITY_REFERENCE, e);
        } catch (DeletedObjectNotExistsException e) {
            return maintenanceDAO.rollback(id, DELETED_OBJECT_NOT_EXISTS, e);
        } catch (WrongEntityIdException e) {
            return maintenanceDAO.rollback(id, WRONG_DELETED_ID, e);
        } catch (IllegalArgumentException e) {
            return maintenanceDAO.rollback(id, NULL_ID, e);
        }
        maintenanceDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertMaintenance(Maintenance maintenance) {
        if (maintenance == null) return null;
        Integer maintenanceId = null;
        try {
            maintenanceId = maintenanceDAO.insertMaintenance(maintenance);
            LOGGER.info("Get from DB maintenanceId = " + maintenanceId);
        } catch (IllegalArgumentException e) {
            return maintenanceDAO.rollback(WRONG_FIELD, e);
        }
        maintenanceDAO.commit();
        return new IUDAnswer(maintenanceId,true);
    }

    @Override
    public IUDAnswer updateMaintenance(Integer id, Maintenance newMaintenance) {
        if (newMaintenance == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            newMaintenance.setObjectId(id);
            Maintenance oldMaintenance = maintenanceDAO.getMaintenance(id);
            maintenanceDAO.updateMaintenance(newMaintenance, oldMaintenance);
        } catch (IllegalArgumentException e) {
            return maintenanceDAO.rollback(WRONG_FIELD, e);
        }
        maintenanceDAO.commit();
        return new IUDAnswer(id,true);
    }
}
