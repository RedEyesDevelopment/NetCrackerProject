package projectpackage.repository.maintenancedao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.AbstractDAO;
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
            return (Maintenance) manager.createReactEAV(Maintenance.class).getSingleEntityWithId(id);
    }

    @Override
    public List<Maintenance> getAllMaintenances() {
            return manager.createReactEAV(Maintenance.class).getEntityCollection();
    }

    @Override
    public int insertMaintenance(Maintenance maintenance) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 14, null, null);

            jdbcTemplate.update(insertAttribute, 47, objectId, maintenance.getMaintenanceTitle(), null);
            jdbcTemplate.update(insertAttribute, 48, objectId, maintenance.getMaintenanceType(), null);
            jdbcTemplate.update(insertAttribute, 49, objectId, maintenance.getMaintenancePrice(), null);

        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void updateMaintenance(Maintenance newMaintenance, Maintenance oldMaintenance) throws TransactionException {
        try {
            if (!oldMaintenance.getMaintenanceTitle().equals(newMaintenance.getMaintenanceTitle())) {
                jdbcTemplate.update(updateAttribute, newMaintenance.getMaintenanceTitle(), null, newMaintenance.getObjectId(), 47);
            }
            if (!oldMaintenance.getMaintenanceType().equals(newMaintenance.getMaintenanceType())) {
                jdbcTemplate.update(updateAttribute, newMaintenance.getMaintenanceType(), null, newMaintenance.getObjectId(), 48);
            }
            if (!oldMaintenance.getMaintenancePrice().equals(newMaintenance.getMaintenancePrice())) {
                jdbcTemplate.update(updateAttribute, newMaintenance.getMaintenancePrice(), null, newMaintenance.getObjectId(), 49);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
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
}
