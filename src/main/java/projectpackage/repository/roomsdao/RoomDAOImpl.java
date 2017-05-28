package projectpackage.repository.roomsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.ratesdao.RateDAOImpl;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.PriceEqualsToRoomCondition;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

@Repository
public class RoomDAOImpl extends AbstractDAO implements RoomDAO{
    private static final Logger LOGGER = Logger.getLogger(RateDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

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
    public void deleteRoom(int id) throws ReferenceBreakException {
        deleteSingleEntityById(id);
    }
}
