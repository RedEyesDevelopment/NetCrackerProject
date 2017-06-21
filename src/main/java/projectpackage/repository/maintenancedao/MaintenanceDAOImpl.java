package projectpackage.repository.maintenancedao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
@Repository
public class MaintenanceDAOImpl extends AbstractDAO implements MaintenanceDAO {
    private static final Logger LOGGER = Logger.getLogger(MaintenanceDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Maintenance getMaintenance(Integer id) {
        if (null == id) return null;
        try {
            return (Maintenance) manager.createReactEAV(Maintenance.class).getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Maintenance> getAllMaintenances() {
        try {
            return manager.createReactEAV(Maintenance.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public Integer insertMaintenance(Maintenance maintenance) throws TransactionException {
        if (maintenance == null) return null;
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(INSERT_OBJECT, objectId, null, 14, null, null);
            if (maintenance.getMaintenanceTitle() != null && !maintenance.getMaintenanceTitle().isEmpty()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 47, objectId, maintenance.getMaintenanceTitle(), null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 47, objectId, null, null);
            }
            if (maintenance.getMaintenanceType() != null && !maintenance.getMaintenanceType().isEmpty()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 48, objectId, maintenance.getMaintenanceType(), null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 48, objectId, null, null);
            }
            jdbcTemplate.update(INSERT_ATTRIBUTE, 49, objectId, maintenance.getMaintenancePrice(), null);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public Integer updateMaintenance(Maintenance newMaintenance, Maintenance oldMaintenance) throws TransactionException {
        if (newMaintenance == null || oldMaintenance == null) return null;
        try {
            updateTitle(newMaintenance, oldMaintenance);
            updateType(newMaintenance, oldMaintenance);
            updatePrice(newMaintenance, oldMaintenance);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }

        return newMaintenance.getObjectId();
    }

    @Override
    public void deleteMaintenance(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        Maintenance maintenance = null;
        try {
            maintenance = getMaintenance(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == maintenance) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }

    private void updateTitle(Maintenance newMaintenance, Maintenance oldMaintenance) {
        if (oldMaintenance.getMaintenanceTitle() != null && newMaintenance.getMaintenanceTitle() != null
                && !newMaintenance.getMaintenanceTitle().isEmpty()) {
            if (!oldMaintenance.getMaintenanceTitle().equals(newMaintenance.getMaintenanceTitle())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newMaintenance.getMaintenanceTitle(),
                        null, newMaintenance.getObjectId(), 47);
            }
        } else if (oldMaintenance.getMaintenanceTitle() != null || newMaintenance.getMaintenanceTitle() != null) {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, null, null, newMaintenance.getObjectId(), 47);
        }
    }

    private void updateType(Maintenance newMaintenance, Maintenance oldMaintenance) {
        if (oldMaintenance.getMaintenanceType() != null && newMaintenance.getMaintenanceTitle() != null
                && !newMaintenance.getMaintenanceType().isEmpty()) {
            if (!oldMaintenance.getMaintenanceType().equals(newMaintenance.getMaintenanceType())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newMaintenance.getMaintenanceType(),
                        null, newMaintenance.getObjectId(), 48);
            }
        } else if (oldMaintenance.getMaintenanceType() != null || newMaintenance.getMaintenanceType() != null) {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, null, null, newMaintenance.getObjectId(), 48);
        }
    }

    private void updatePrice(Maintenance newMaintenance, Maintenance oldMaintenance) {
        if (oldMaintenance.getMaintenancePrice() != null && newMaintenance.getMaintenancePrice() != null) {
            if (!oldMaintenance.getMaintenancePrice().equals(newMaintenance.getMaintenancePrice())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newMaintenance.getMaintenancePrice(),
                        null, newMaintenance.getObjectId(), 49);
            }
        } else if (oldMaintenance.getMaintenancePrice() != null || newMaintenance.getMaintenancePrice() != null) {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, newMaintenance.getMaintenancePrice(),
                    null, newMaintenance.getObjectId(), 49);
        }
    }
}
