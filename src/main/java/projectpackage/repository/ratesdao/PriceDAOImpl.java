package projectpackage.repository.ratesdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.RequiredFieldAbsenceException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Repository
public class PriceDAOImpl extends AbstractDAO implements PriceDAO {
    private static final Logger LOGGER = Logger.getLogger(PriceDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Price getPrice(Integer id) {
        if (null == id) return null;
        try {
            return (Price) manager.createReactEAV(Price.class).getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Price> getAllPrices() {
        try {
            return manager.createReactEAV(Price.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public Integer updatePrice(Price newPrice, Price oldPrice) throws TransactionException {
        if (newPrice == null || oldPrice == null) return null;
        try {
            updateNumberOfPeople(newPrice, oldPrice);
            updateRate(newPrice, oldPrice);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return newPrice.getObjectId();
    }

    private void updateNumberOfPeople(Price newPrice, Price oldPrice) {
        if (oldPrice.getNumberOfPeople() != null && newPrice.getNumberOfPeople() != null) {
            if (!oldPrice.getNumberOfPeople().equals(newPrice.getNumberOfPeople())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newPrice.getNumberOfPeople(), null,
                        newPrice.getObjectId(), 32);
            }
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }

    private void updateRate(Price newPrice, Price oldPrice) {
        if (oldPrice.getRate() != null && newPrice.getRate() != null) {
            if (!oldPrice.getRate().equals(newPrice.getRate())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newPrice.getRate(), null,
                        newPrice.getObjectId(), 33);
            }
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }
}
