package projectpackage.repository.ratesdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class RateDAOImpl extends AbstractDAO implements RateDAO{
    private static final Logger LOGGER = Logger.getLogger(RateDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Rate getRate(Integer id) {
        if (null == id) return null;

        return (Rate) manager.createReactEAV(Rate.class).fetchRootChild(Price.class).closeAllFetches()
                .getSingleEntityWithId(id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Rate> getAllRates() {
        return manager.createReactEAV(Rate.class).fetchRootChild(Price.class).closeAllFetches().getEntityCollection();
    }

    @Transactional
    @Override
    public Integer insertRate(Rate rate) {
        if (rate == null) return null;
        Long price1 = null;
        Long price2 = null;
        Long price3 = null;
        for (Price price : rate.getPrices()) {
            if (price.getNumberOfPeople().equals(1)) price1 = price.getRate();
            if (price.getNumberOfPeople().equals(2)) price2 = price.getRate();
            if (price.getNumberOfPeople().equals(3)) price3 = price.getRate();
        }
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withCatalogName("Rate_tools")
                .withFunctionName("new_rate");
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("in_price1_price", price1);
        in.addValue("in_price2_price", price2);
        in.addValue("in_price3_price", price3);
        in.addValue("in_room_type_id", rate.getRoomTypeId());
        in.addValue("in_new_rate_start", rate.getRateFromDate());
        in.addValue("in_new_rate_finish", rate.getRateToDate());
        BigDecimal insertedRateId = call.executeFunction(BigDecimal.class, in);

        return insertedRateId.intValue();
    }

    @Transactional
    @Override
    public void deleteRate(Integer id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        if (id == null) throw new IllegalArgumentException();
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
