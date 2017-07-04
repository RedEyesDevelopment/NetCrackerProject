package projectpackage.repository.roomsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.ratesdao.RateDAOImpl;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.PriceEqualsToRoomCondition;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
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
        if (null==id) {
            return null;
        }

        return (Room) manager.createReactEAV(Room.class).addCondition(new PriceEqualsToRoomCondition(), ConditionExecutionMoment.AFTER_QUERY)
                .fetchRootReference(RoomType.class, "RoomTypeToRoom")
                .fetchInnerChild(Rate.class).fetchInnerChild(Price.class).closeAllFetches()
                .getSingleEntityWithId(id);
    }

    @Override
    public Room getSimpleRoom(Integer id) {
        if (null==id) {
            return null;
        }

        return (Room) manager.createReactEAV(Room.class)
                .fetchRootReference(RoomType.class, "RoomTypeToRoom").closeAllFetches().getSingleEntityWithId(id);
    }

    @Override
    public List<Room> getAllRooms() {
        return manager.createReactEAV(Room.class).addCondition(new PriceEqualsToRoomCondition(), ConditionExecutionMoment.AFTER_QUERY)
                .fetchRootReference(RoomType.class, "RoomTypeToRoom")
                .fetchInnerChild(Rate.class).fetchInnerChild(Price.class).closeAllFetches().getEntityCollection();
    }

    @Override
    public List<Room> getSimpleRoomsList() {
        return manager.createReactEAV(Room.class).fetchRootReference(RoomType.class, "RoomTypeToRoom").closeAllFetches().getEntityCollection();
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
    public Integer insertRoom(Room room) {
        if (room == null) {
            return null;
        }
        Integer objectId = nextObjectId();

        jdbcTemplate.update(INSERT_OBJECT, objectId, null, 1, null, null);
        insertRoomNumber(room, objectId);
        insertNumberOfResidents(room, objectId);
        insertRoomType(room, objectId);

        return objectId;
    }

    @Override
    public Integer updateRoom(Room newRoom, Room oldRoom) {
        if (oldRoom == null || newRoom == null) {
            return null;
        }

        updateRoomNumber(newRoom, oldRoom);
        updateNumberOfResidents(newRoom, oldRoom);
        updateRoomType(newRoom, oldRoom);

        return newRoom.getObjectId();
    }

    @Override
    public void deleteRoom(Integer id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        Room room = null;
        try {
            room = getRoom(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == room) {
            throw new DeletedObjectNotExistsException(this);
        }


        deleteSingleEntityById(id);
    }

    private void insertRoomType(Room room, Integer objectId) {
        if (room.getRoomType() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 4, objectId, room.getRoomType().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertNumberOfResidents(Room room, Integer objectId) {
        if (room.getNumberOfResidents() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 2, objectId, room.getNumberOfResidents(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertRoomNumber(Room room, Integer objectId) {
        if (room.getRoomNumber() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 1, objectId, room.getRoomNumber(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }


    private void updateRoomNumber(Room newRoom, Room oldRoom) {
        if (oldRoom.getRoomNumber() != null && newRoom.getRoomNumber() != null) {
            if (!oldRoom.getRoomNumber().equals(newRoom.getRoomNumber())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newRoom.getRoomNumber(), null, newRoom.getObjectId(), 1);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateNumberOfResidents(Room newRoom, Room oldRoom) {
        if (oldRoom.getNumberOfResidents() != null && newRoom.getNumberOfResidents() != null) {
            if (!oldRoom.getNumberOfResidents().equals(newRoom.getNumberOfResidents())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newRoom.getNumberOfResidents(), null, newRoom.getObjectId(), 2);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateRoomType(Room newRoom, Room oldRoom) {
        if (oldRoom.getRoomType() != null && newRoom.getRoomType() != null) {
            if (oldRoom.getRoomType().getObjectId() != newRoom.getRoomType().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newRoom.getRoomType().getObjectId(), newRoom.getObjectId(), 4);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
