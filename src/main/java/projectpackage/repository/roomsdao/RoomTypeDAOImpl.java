package projectpackage.repository.roomsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public Integer insertRoomType(RoomType roomType) {
        if (roomType == null) return null;
        Integer objectId = nextObjectId();

        jdbcTemplate.update(INSERT_OBJECT, objectId, null, 5, null, null);
        insertTitle(roomType, objectId);
        insertContent(roomType, objectId);
        createRateForNewRoomType(objectId);

        return objectId;
    }

    @Override
    public Integer updateRoomType(RoomType newRoomType, RoomType oldRoomType) {
        if (oldRoomType == null || newRoomType == null) return null;

        updateTitle(newRoomType, oldRoomType);
        updateContent(newRoomType, oldRoomType);

        return newRoomType.getObjectId();
    }

    @Override
    public void deleteRoomType(Integer id) {
        if (id == null) throw new IllegalArgumentException();
        RoomType roomType = null;
        try {
            roomType = getRoomType(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == roomType) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }

    private void createRateForNewRoomType(Integer objectId) {
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

    private void insertContent(RoomType roomType, Integer objectId) {
        if (roomType.getContent() != null && !roomType.getContent().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 29, objectId, roomType.getContent(), null);
        } else {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 29, objectId, null, null);
        }
    }

    private void insertTitle(RoomType roomType, Integer objectId) {
        if (roomType.getRoomTypeTitle() != null && !roomType.getRoomTypeTitle().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 28, objectId, roomType.getRoomTypeTitle(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateTitle(RoomType newRoomType, RoomType oldRoomType) {
        if (oldRoomType.getRoomTypeTitle() != null && newRoomType.getRoomTypeTitle() != null
                && !newRoomType.getRoomTypeTitle().isEmpty()) {
            if (!oldRoomType.getRoomTypeTitle().equals(newRoomType.getRoomTypeTitle())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newRoomType.getRoomTypeTitle(), null, newRoomType.getObjectId(), 28);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateContent(RoomType newRoomType, RoomType oldRoomType) {
        if (oldRoomType.getContent() != null && newRoomType.getContent() != null && !newRoomType.getContent().isEmpty()) {
            if (!oldRoomType.getContent().equals(newRoomType.getContent())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newRoomType.getContent(), null, newRoomType.getObjectId(), 29);
            }
        } else if (newRoomType.getContent() != null && !newRoomType.getContent().isEmpty()) {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, newRoomType.getContent(), null, newRoomType.getObjectId(), 29);
        } else if (oldRoomType.getContent() != null) {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, null, null, newRoomType.getObjectId(), 29);
        }
    }
}
