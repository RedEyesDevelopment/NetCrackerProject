package projectpackage.repository.maintenancedao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;


@Repository
public class ComplimentaryDAOImpl extends AbstractDAO implements ComplimentaryDAO {

    private static final Logger LOGGER = Logger.getLogger(ComplimentaryDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Complimentary getComplimentary(Integer id) {
        if (id == null) {
            return null;
        }

        return (Complimentary) manager.createReactEAV(Complimentary.class)
                .fetchRootReference(Maintenance.class, "MaintenanceToComplimentary")
                .closeAllFetches().getSingleEntityWithId(id);
    }

    @Override
    public List<Complimentary> getAllComplimentaries() {
        return manager.createReactEAV(Complimentary.class)
                .fetchRootReference(Maintenance.class, "MaintenanceToComplimentary")
                .closeAllFetches().getEntityCollection();
    }

    @Override
    public Integer insertComplimentary(Complimentary complimentary) {
        if (complimentary == null) {
            return null;
        }
        Integer objectId = nextObjectId();
        jdbcTemplate.update(INSERT_OBJECT, objectId, complimentary.getCategoryId(), 15, null, null);
        insertMaintenance(complimentary, objectId);

        return objectId;
    }

    @Override
    public Integer updateComplimentary(Complimentary newComplimentary, Complimentary oldComplimentary) {
        if (oldComplimentary == null || newComplimentary == null) {
            return null;
        }

        updateMaintenance(newComplimentary, oldComplimentary);
        return newComplimentary.getObjectId();
    }

    @Override
    public void deleteComplimentary(Integer id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        Complimentary complimentary = null;
        try {
            complimentary = getComplimentary(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == complimentary) {
            throw new DeletedObjectNotExistsException(this);
        }

        deleteSingleEntityById(id);
    }

    private void updateMaintenance(Complimentary newComplimentary, Complimentary oldComplimentary) {
        if (oldComplimentary.getMaintenance() != null && newComplimentary.getMaintenance() != null) {
            if (oldComplimentary.getMaintenance().getObjectId() != newComplimentary.getMaintenance().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newComplimentary.getMaintenance().getObjectId(),
                        newComplimentary.getObjectId(), 51);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertMaintenance(Complimentary complimentary, Integer objectId) {
        if (complimentary.getMaintenance() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 51, objectId, complimentary.getMaintenance().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
