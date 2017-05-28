package projectpackage.repository.roomsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.rowmappers.IdRowMapper;

import java.sql.Types;
import java.util.*;

@Repository
public class RoomTypeDAOImpl extends AbstractDAO implements RoomTypeDAO {
    private static final Logger LOGGER = Logger.getLogger(RoomTypeDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public RoomType getRoomType(Integer id) {
        if (null == id) return null;
        try {
            return (RoomType) manager.createReactEAV(RoomType.class).fetchRootChild(Rate.class)
                    .fetchInnerChild(Price.class).closeAllFetches().getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        try {
            return manager.createReactEAV(RoomType.class).fetchRootChild(Rate.class).fetchInnerChild(Price.class)
                    .closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
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
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("Room_tools.get_cost_living")
                .declareParameters(
                        new SqlParameter("in_room_type_obj_id", Types.BIGINT),
                        new SqlParameter("in_number_of_residents", Types.BIGINT),
                        new SqlParameter("in_date_start", Types.DATE),
                        new SqlParameter("in_date_finish", Types.DATE),
                        new SqlOutParameter("cost", Types.BIGINT));
        Map<String, Object> execute = call.execute(
                new MapSqlParameterSource("in_room_type_obj_id", roomType.getObjectId()),
                new MapSqlParameterSource("in_number_of_residents", numberOfResidents),
                new MapSqlParameterSource("in_date_start", start),
                new MapSqlParameterSource("in_date_finish", finish));
        return (long) execute.get("cost");
    }

    @Override
    public int insertRoomType(RoomType roomType) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 5, null, null);

            jdbcTemplate.update(insertAttribute, 28, objectId, roomType.getRoomTypeTitle(), null);
            jdbcTemplate.update(insertAttribute, 29, objectId, roomType.getContent(), null);
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
        return objectId;
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
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
    }

    @Override
    public void deleteRoomType(int id) throws ReferenceBreakException {
        deleteSingleEntityById(id);
    }
}
