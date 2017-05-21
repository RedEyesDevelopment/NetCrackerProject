package projectpackage.repository.ratesdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

@Repository
public class RateDAOImpl extends AbstractDAO implements RateDAO{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Rate getRate(Integer id) {
        if (null==id) return null;
        try {
            return (Rate) manager.createReactEAV(Rate.class).fetchChildEntityCollection(Price.class).closeAllFetches().getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            return null;
        }
    }

    @Override
    public List<Rate> getAllRates() {
        try {
            return manager.createReactEAV(Rate.class).fetchChildEntityCollection(Price.class).closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
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
        } catch (NullPointerException e) {
            throw new TransactionException(this);
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
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
    }

    @Override
    public int deleteRate(int id) {
        return deleteSingleEntityById(id);
    }
}
