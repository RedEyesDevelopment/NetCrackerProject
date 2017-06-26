package projectpackage.repository.roomsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.ratesdao.RateDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.rowmappers.IdRowMapper;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class RoomTypeDAOImpl extends AbstractDAO implements RoomTypeDAO {
    private static final Logger LOGGER = Logger.getLogger(RoomTypeDAOImpl.class);

    @Value("${default.price}")
    private String defaultPrice;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RateDAO rateDAO;


    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public RoomType getRoomType(Integer id) {
        if (null == id) return null;
            return (RoomType) manager.createReactEAV(RoomType.class).fetchRootChild(Rate.class)
                    .fetchInnerChild(Price.class).closeAllFetches().getSingleEntityWithId(id);
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
            return manager.createReactEAV(RoomType.class).fetchRootChild(Rate.class).fetchInnerChild(Price.class)
                    .closeAllFetches().getEntityCollection();
    }

    @Override
    public Set<Integer> getAvailableRoomTypes(int numberOfPeople, Date startDate, Date finishDate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("num_of_res", numberOfPeople);
        parameters.put("d_start", startDate);
        parameters.put("d_finish", finishDate);
        List ids = namedParameterJdbcTemplate.query("SELECT * FROM TABLE(Room_tools.get_free_room_types(" +
                ":num_of_res, :d_start, :d_finish))", parameters, new IdRowMapper());
        Set<Integer> result = new HashSet<>();
        result.addAll(ids);
        return result;
    }

    @Override
    public long getCostForLiving(RoomType roomType, int numberOfResidents, Date start, Date finish) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withCatalogName("Room_tools").withFunctionName("get_cost_living");
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("in_room_type_obj_id", roomType.getObjectId());
        in.addValue("in_number_of_residents", numberOfResidents);
        in.addValue("in_date_start", start);
        in.addValue("in_date_finish", finish);
        BigDecimal bigDecimal = call.executeFunction(BigDecimal.class, in);

        return bigDecimal.longValue();
    }

    @Override
    public int insertRoomType(RoomType roomType) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 5, null, null);

            jdbcTemplate.update(insertAttribute, 28, objectId, roomType.getRoomTypeTitle(), null);
            jdbcTemplate.update(insertAttribute, 29, objectId, roomType.getContent(), null);

            createRateForNewRoomType(objectId);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    private void createRateForNewRoomType(Integer objectId) throws TransactionException {
        Rate rate = new Rate();
        rate.setRoomTypeId(objectId);
        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        rate.setRateFromDate(new GregorianCalendar(currYear,0,1).getTime());
        rate.setRateToDate(new GregorianCalendar(currYear + 2, 11, 31).getTime());

        Set<Price> prices = new HashSet<>(3);

        Price price1 = new Price();
        price1.setNumberOfPeople(1);
        price1.setRate(Long.parseLong(defaultPrice));
        prices.add(price1);

        Price price2 = new Price();
        price2.setNumberOfPeople(2);
        price2.setRate(200000L);
        prices.add(price2);

        Price price3 = new Price();
        price3.setNumberOfPeople(3);
        price3.setRate(300000L);
        prices.add(price3);

        rate.setPrices(prices);

        rateDAO.insertRate(rate);
    }

    @Override
    public void updateRoomType(RoomType newRoomType, RoomType oldRoomType) throws TransactionException {
        try {
            if (!oldRoomType.getRoomTypeTitle().equals(newRoomType.getRoomTypeTitle())) {
                jdbcTemplate.update(updateAttribute, newRoomType.getRoomTypeTitle(), null, newRoomType.getObjectId(), 28);
            }
            if (!oldRoomType.getContent().equals(newRoomType.getContent())) {
                jdbcTemplate.update(updateAttribute, newRoomType.getContent(), null, newRoomType.getObjectId(), 29);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
    }

    @Override
    public void deleteRoomType(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        RoomType roomType = null;
        try {
            roomType = getRoomType(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == roomType) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }
}
