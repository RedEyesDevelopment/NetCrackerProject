package projectpackage.repository.ratesdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

@Repository
public class RateDAOImpl extends AbstractDAO implements RateDAO{
    private static final Logger LOGGER = Logger.getLogger(RateDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Rate getRate(Integer id) {
        if (null == id) return null;
        try {
            return (Rate) manager.createReactEAV(Rate.class).fetchRootChild(Price.class).closeAllFetches()
                    .getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Rate> getAllRates() {
        try {
            return manager.createReactEAV(Rate.class).fetchRootChild(Price.class).closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public int insertRate(Rate rate) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, rate.getRoomTypeId(), 6, null, null);
            jdbcTemplate.update(insertAttribute, 30, objectId, null, rate.getRateFromDate());
            jdbcTemplate.update(insertAttribute, 31, objectId, null, rate.getRateToDate());
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void updateRate(Rate newRate, Rate oldRate) throws TransactionException {
        try {
            if (oldRate.getRateFromDate().getTime() != newRate.getRateFromDate().getTime()) {
                jdbcTemplate.update(updateAttribute, null, newRate.getRateFromDate(),
                        newRate.getObjectId(), 30);
            }
            if (oldRate.getRateToDate().getTime() != newRate.getRateToDate().getTime()) {
                jdbcTemplate.update(updateAttribute, null, newRate.getRateToDate(),
                        newRate.getObjectId(), 31);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
    }

    @Override
    public void deleteRate(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        Rate rate = null;
        try {
            rate = getRate(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == rate) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }
}
