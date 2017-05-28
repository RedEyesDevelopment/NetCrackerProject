package projectpackage.repository.maintenancedao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.daoexceptions.WrongEntityIdException;
import projectpackage.repository.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;


@Repository
public class ComplimentaryDAOImpl extends AbstractDAO implements ComplimentaryDAO {

    private static final Logger LOGGER = Logger.getLogger(ComplimentaryDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Complimentary getComplimentary(Integer id) {
        if (id == null) return null;
        try {
            return (Complimentary) manager.createReactEAV(Complimentary.class)
                    .fetchRootReference(Maintenance.class, "MaintenanceToComplimentary")
                    .closeAllFetches().getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Complimentary> getAllComplimentaries() {
        try {
            return manager.createReactEAV(Complimentary.class)
                    .fetchRootReference(Maintenance.class, "MaintenanceToComplimentary")
                    .closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public int insertComplimentary(Complimentary complimentary) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, complimentary.getCategoryId(), 15, null, null);
            jdbcTemplate.update(insertObjReference, 51, objectId, complimentary.getMaintenance().getObjectId());
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void updateComplimentary(Complimentary newComplimentary, Complimentary oldComplimentary) throws TransactionException {
        try {
            if (oldComplimentary.getMaintenance().getObjectId() != newComplimentary.getMaintenance().getObjectId()) {
                jdbcTemplate.update(updateReference, newComplimentary.getMaintenance().getObjectId(), newComplimentary.getObjectId(), 51);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());

        }
    }

    @Override
    public void deleteComplimentary(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        Complimentary complimentary = null;
        try {
            complimentary = getComplimentary(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == complimentary) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }
}
