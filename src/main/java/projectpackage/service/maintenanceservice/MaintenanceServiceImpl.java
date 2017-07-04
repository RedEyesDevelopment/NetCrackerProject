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

@Log4j
@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    private static final Logger LOGGER = Logger.getLogger(MaintenanceServiceImpl.class);

    @Autowired
    MaintenanceDAO maintenanceDAO;

    @Override
    public List<Maintenance> getAllMaintenances() {
        return maintenanceDAO.getAllMaintenances();
    }

    @Override
    public Maintenance getSingleMaintenanceById(Integer id) {
        return maintenanceDAO.getMaintenance(id);
    }

    @Override
    public IUDAnswer deleteMaintenance(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        try {
            maintenanceDAO.deleteMaintenance(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage());
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new IUDAnswer(id, false, NULL_ID, e.getMessage());
        }
        maintenanceDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertMaintenance(Maintenance maintenance) {
        if (maintenance == null) {
            return null;
        }
        Integer maintenanceId = null;
        try {
            maintenanceId = maintenanceDAO.insertMaintenance(maintenance);
            LOGGER.info("Get from DB maintenanceId = " + maintenanceId);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
        }
        maintenanceDAO.commit();
        return new IUDAnswer(maintenanceId,true);
    }

    @Override
    public IUDAnswer updateMaintenance(Integer id, Maintenance newMaintenance) {
        if (newMaintenance == null) {
            return null;
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        try {
            newMaintenance.setObjectId(id);
            Maintenance oldMaintenance = maintenanceDAO.getMaintenance(id);
            maintenanceDAO.updateMaintenance(newMaintenance, oldMaintenance);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(id, false, WRONG_FIELD, e.getMessage());
        }
        maintenanceDAO.commit();
        return new IUDAnswer(id,true);
    }
}
