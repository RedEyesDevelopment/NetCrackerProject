package projectpackage.repository.maintenancedao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;

@Repository
public class MaintenanceDAOImpl extends AbstractDAO implements MaintenanceDAO {
    private static final Logger LOGGER = Logger.getLogger(MaintenanceDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Maintenance getMaintenance(Integer id) {
        if (null == id) {
            return null;
        }

        return (Maintenance) manager.createReactEAV(Maintenance.class).getSingleEntityWithId(id);

    }

    @Override
    public List<Maintenance> getAllMaintenances() {
        return manager.createReactEAV(Maintenance.class).getEntityCollection();
    }

    @Override
    public Integer insertMaintenance(Maintenance maintenance) {
        if (maintenance == null) {
            return null;
        }
        Integer objectId = nextObjectId();
        jdbcTemplate.update(INSERT_OBJECT, objectId, null, 14, null, null);
        insertTitle(maintenance, objectId);
        insertType(maintenance, objectId);
        insertPrice(maintenance, objectId);

        return objectId;
    }

    @Override
    public Integer updateMaintenance(Maintenance newMaintenance, Maintenance oldMaintenance) {
        if (newMaintenance == null || oldMaintenance == null) {
            return null;
        }

        updateTitle(newMaintenance, oldMaintenance);
        updateType(newMaintenance, oldMaintenance);
        updatePrice(newMaintenance, oldMaintenance);

        return newMaintenance.getObjectId();
    }

    @Override
    public void deleteMaintenance(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        Maintenance maintenance = null;
        try {
            maintenance = getMaintenance(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == maintenance) {
            throw new DeletedObjectNotExistsException(this);
        }

        deleteSingleEntityById(id);
    }

    private void insertPrice(Maintenance maintenance, Integer objectId) {
        if (maintenance.getMaintenancePrice() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 49, objectId, maintenance.getMaintenancePrice(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertType(Maintenance maintenance, Integer objectId) {
        if (maintenance.getMaintenanceType() != null && !maintenance.getMaintenanceType().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 48, objectId, maintenance.getMaintenanceType(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertTitle(Maintenance maintenance, Integer objectId) {
        if (maintenance.getMaintenanceTitle() != null && !maintenance.getMaintenanceTitle().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 47, objectId, maintenance.getMaintenanceTitle(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateTitle(Maintenance newMaintenance, Maintenance oldMaintenance) {
        if (oldMaintenance.getMaintenanceTitle() != null && newMaintenance.getMaintenanceTitle() != null
                && !newMaintenance.getMaintenanceTitle().isEmpty()) {
            if (!oldMaintenance.getMaintenanceTitle().equals(newMaintenance.getMaintenanceTitle())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newMaintenance.getMaintenanceTitle(),
                        null, newMaintenance.getObjectId(), 47);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateType(Maintenance newMaintenance, Maintenance oldMaintenance) {
        if (oldMaintenance.getMaintenanceType() != null && newMaintenance.getMaintenanceTitle() != null
                && !newMaintenance.getMaintenanceType().isEmpty()) {
            if (!oldMaintenance.getMaintenanceType().equals(newMaintenance.getMaintenanceType())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newMaintenance.getMaintenanceType(),
                        null, newMaintenance.getObjectId(), 48);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updatePrice(Maintenance newMaintenance, Maintenance oldMaintenance) {
        if (oldMaintenance.getMaintenancePrice() != null && newMaintenance.getMaintenancePrice() != null) {
            if (!oldMaintenance.getMaintenancePrice().equals(newMaintenance.getMaintenancePrice())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newMaintenance.getMaintenancePrice(),
                        null, newMaintenance.getObjectId(), 49);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
