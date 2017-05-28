package projectpackage.repository.ratesdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
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
    public int insertPrice(Price price) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, price.getRateId(), 7, null, null);
            jdbcTemplate.update(insertAttribute, 32, objectId, price.getNumberOfPeople(), null);
            jdbcTemplate.update(insertAttribute, 33, objectId, price.getRate(), null);
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
        return objectId;
    }

    @Override
    public void updatePrice(Price newPrice, Price oldPrice) throws TransactionException {
        try {
            if (!oldPrice.getNumberOfPeople().equals(newPrice.getNumberOfPeople())) {
                jdbcTemplate.update(updateAttribute, newPrice.getNumberOfPeople(), null,
                        newPrice.getObjectId(), 32);
            }
            if (!oldPrice.getRate().equals(newPrice.getRate())) {
                jdbcTemplate.update(updateAttribute, newPrice.getRate(), null,
                        newPrice.getObjectId(), 33);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
    }

    @Override
    public void deletePrice(int id) throws ReferenceBreakException {
        deleteSingleEntityById(id);
    }
}
