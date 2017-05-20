package projectpackage.repository.ratesdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Repository
public class PriceDAOImpl extends AbstractDAO implements PriceDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Price getPrice(Integer id) {
        if (null == id) return null;
        try {
            return (Price) manager.createReactEAV(Price.class).getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            return null;
        }
    }

    @Override
    public List<Price> getAllPrice() {
        try {
            return manager.createReactEAV(Price.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            return null;
        }
    }

    @Override
    public int insertPrice(Price price) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            //7 = Price
            jdbcTemplate.update(insertObjects, objectId, price.getRateId(), 7, null, null);
            //32 = Number_of_people
            jdbcTemplate.update(insertAttributes, 32, objectId, price.getNumberOfPeople(), null);
            //33 = Rate
            jdbcTemplate.update(insertAttributes, 33, objectId, price.getRate(), null);
        } catch (NullPointerException e) {
            throw new TransactionException(price);
        }
        return objectId;
    }

    @Override
    public void updatePrice(Price newPrice, Price oldPrice) throws TransactionException {
        try {
            if (!oldPrice.getNumberOfPeople().equals(newPrice.getNumberOfPeople())) {
                jdbcTemplate.update(updateAttributes, newPrice.getNumberOfPeople(), null,
                        newPrice.getObjectId(), 32);
            }
            if (!oldPrice.getRate().equals(newPrice.getRate())) {
                jdbcTemplate.update(updateAttributes, newPrice.getRate(), null,
                        newPrice.getObjectId(), 33);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(newPrice);
        }
    }

    @Override
    public int deletePrice(int id) {
        return deleteSingleEntityById(id);
    }
}
