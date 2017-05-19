package projectpackage.repository.ratesdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import projectpackage.model.rates.Rate;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;


public class RateDAOImpl extends AbstractDAO implements RateDAO{

    @Autowired
    JdbcTemplate jdbcTemplate;



    @Override
    public int insertRate(Rate rate) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            //6 = Rate
            jdbcTemplate.update(insertObjects, objectId, rate.getRoomTypeId(), 6, null, null);
            //30 = Rate_from_date
            jdbcTemplate.update(insertAttributes, 30, objectId, null, rate.getRateFromDate());
            //31 = Rate_to_date
            jdbcTemplate.update(insertAttributes, 31, objectId, null, rate.getRateToDate());
            //44 = Creation_date
            jdbcTemplate.update(insertAttributes, 44, objectId, null, rate.getCreationDate());
        } catch (NullPointerException e) {
            throw new TransactionException(rate);
        }
        return objectId;
    }

    @Override
    public void updateRate(Rate newRate, Rate oldRate) throws TransactionException {
        try {
            if (oldRate.getRateFromDate().getTime() != newRate.getRateFromDate().getTime()) {
                jdbcTemplate.update(updateAttributes, null, newRate.getRateFromDate(),
                        newRate.getObjectId(), 30);
            }
            if (oldRate.getRateToDate().getTime() != newRate.getRateToDate().getTime()) {
                jdbcTemplate.update(updateAttributes, null, newRate.getRateToDate(),
                        newRate.getObjectId(), 31);
            }
            if (oldRate.getCreationDate().getTime() != newRate.getCreationDate().getTime()) {
                jdbcTemplate.update(updateAttributes, null, newRate.getCreationDate(),
                        newRate.getObjectId(), 44);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(newRate);
        }
    }

    @Override
    public int deleteRate(int id) {
        return deleteSingleEntityById(id);
    }
}
