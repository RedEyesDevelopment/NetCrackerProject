package projectpackage.repository.roomsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.conditions.PriceEqualsToRoomCondition;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.ratesdao.RateDAOImpl;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.support.rowmappers.IdRowMapper;

import java.util.*;

@Repository
public class RoomDAOImpl extends AbstractDAO implements RoomDAO{
    private static final Logger LOGGER = Logger.getLogger(RateDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Room getRoom(Integer id) {
        if (null==id) return null;
        try {
            return (Room) manager.createReactEAV(Room.class).addCondition(new PriceEqualsToRoomCondition(), ConditionExecutionMoment.AFTER_QUERY)
                    .fetchRootReference(RoomType.class, "RoomTypeToRoom")
                    .fetchInnerChild(Rate.class).fetchInnerChild(Price.class).closeAllFetches()
                    .getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Room> getAllRooms() {
        try {
            return manager.createReactEAV(Room.class).addCondition(new PriceEqualsToRoomCondition(), ConditionExecutionMoment.AFTER_QUERY)
                    .fetchRootReference(RoomType.class, "RoomTypeToRoom")
                    .fetchInnerChild(Rate.class).fetchInnerChild(Price.class).closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public Room getFreeRoom(int roomTypeId, int numberOfResidents, Date start, Date finish) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("room_type_id", roomTypeId);
        parameters.put("num_of_res", numberOfResidents);
        parameters.put("d_start", start);
        parameters.put("d_finish", finish);
        List ids = namedParameterJdbcTemplate.query("SELECT * FROM TABLE(Room_tools.get_free_rooms(" +
                ":room_type_id, :num_of_res, :d_start, :d_finish))", parameters, new IdRowMapper());
        if (!ids.isEmpty()) {
            return getRoom((int) ids.get(0));
        } else {
            return null;
        }
    }

    @Override
    public List<Room> getFreeRooms(int roomTypeId, int numberOfResidents, Date start, Date finish) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("room_type_id", roomTypeId);
        parameters.put("num_of_res", numberOfResidents);
        parameters.put("d_start", start);
        parameters.put("d_finish", finish);
        List ids = namedParameterJdbcTemplate.query("SELECT * FROM TABLE(Room_tools.get_free_rooms(" +
                ":room_type_id, :num_of_res, :d_start, :d_finish))", parameters, new IdRowMapper());
        List<Room> allRooms = getAllRooms();
        List<Room> result = new ArrayList<Room>();
        for (int i = 0; i < ids.size(); i++){
            for(int j=0; j < allRooms.size(); j++){
                Room room = allRooms.get(j);
                if(room.getObjectId() == (int)ids.get(i)){
                    result.add(room);
                }
            }
        }
        return result;
    }

    @Override
    public List<Room> getBookedRooms(int roomTypeId, int numberOfResidents, Date start, Date finish) {
        List<Room> freeRooms = getFreeRooms(roomTypeId, numberOfResidents, start, finish);
        List<Room> allRooms = getAllRooms();
        List<Room> result = new ArrayList<Room>();
        for (int i = 0; i < freeRooms.size(); i++){
            for(int j=0; j < allRooms.size(); j++){
                Room room = allRooms.get(j);
                if(room.getObjectId() != freeRooms.get(i).getObjectId()){
                    result.add(room);
                }
            }
        }
        return result;
    }


    @Override
    public int insertRoom(Room room) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 1, null, null);

            jdbcTemplate.update(insertAttribute, 1, objectId, room.getRoomNumber(), null);
            jdbcTemplate.update(insertAttribute, 2, objectId, room.getNumberOfResidents(), null);

            jdbcTemplate.update(insertObjReference, 4, objectId, room.getRoomType().getObjectId());
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void updateRoom(Room newRoom, Room oldRoom) throws TransactionException {
        try {
            if (!oldRoom.getRoomNumber().equals(newRoom.getRoomNumber())) {
                jdbcTemplate.update(updateAttribute, newRoom.getRoomNumber(), null, newRoom.getObjectId(), 1);
            }
            if (!oldRoom.getNumberOfResidents().equals(newRoom.getNumberOfResidents())) {
                jdbcTemplate.update(updateAttribute, newRoom.getNumberOfResidents(), null, newRoom.getObjectId(), 2);
            }
            if (oldRoom.getRoomType().getObjectId() != newRoom.getRoomType().getObjectId()) {
                jdbcTemplate.update(updateReference, newRoom.getRoomType().getObjectId(), newRoom.getObjectId(), 4);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
    }

    @Override
    public void deleteRoom(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        Room room = null;
        try {
            room = getRoom(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == room) throw new DeletedObjectNotExistsException(this);


        deleteSingleEntityById(id);
    }
}
