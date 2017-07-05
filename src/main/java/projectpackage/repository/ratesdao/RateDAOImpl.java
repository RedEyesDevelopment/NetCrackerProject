package projectpackage.repository.ratesdao;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
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
    public Rate getRate(Integer id) {
        if (null == id) {
            return null;
        }

        return (Rate) manager.createReactEAV(Rate.class).fetchRootChild(Price.class).closeAllFetches()
                .getSingleEntityWithId(id);

    }

    @Override
    public List<Rate> getAllRates() {
        return manager.createReactEAV(Rate.class).fetchRootChild(Price.class).closeAllFetches().getEntityCollection();
    }

    @Override
    public Integer insertRate(Rate rate) {
        if (rate == null) {
            throw new IllegalArgumentException();
        }

        DateTime rateFromDate = new DateTime(rate.getRateFromDate()).withHourOfDay(12);
        DateTime rateToDate = new DateTime(rate.getRateToDate()).withHourOfDay(12);

        Long cost1 = null;
        Long cost2 = null;
        Long cost3 = null;
        for (Price price : rate.getPrices()) {
            if (price.getNumberOfPeople().equals(1)) {
                cost1 = price.getRate();
            }
            if (price.getNumberOfPeople().equals(2)) {
                cost2 = price.getRate();
            }
            if (price.getNumberOfPeople().equals(3)) {
                cost3 = price.getRate();
            }
        }
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withCatalogName("Rate_tools")
                .withFunctionName("new_rate");
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("in_price1_price", cost1);
        in.addValue("in_price2_price", cost2);
        in.addValue("in_price3_price", cost3);
        in.addValue("in_room_type_id", rate.getRoomTypeId());
        in.addValue("in_new_rate_start", rateFromDate.toDate());
        in.addValue("in_new_rate_finish", rateToDate.toDate());
        BigDecimal insertedRateId = call.executeFunction(BigDecimal.class, in);

        return insertedRateId.intValue();
    }

    @Override
    public void deleteRate(Integer id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        Rate rate = null;
        try {
            rate = getRate(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == rate) {
            throw new DeletedObjectNotExistsException(this);
        }

        deleteSingleEntityById(id);
    }
}
