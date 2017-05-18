package projectpackage.repository.roomsdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import projectpackage.model.rooms.Room;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomDAOImpl extends AbstractDAO implements RoomDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int insertRoom(Room room) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObjects, objectId, null, 1, null, null);

            jdbcTemplate.update(insertAttributes, 1, objectId, room.getRoomNumber(), null);
            jdbcTemplate.update(insertAttributes, 2, objectId, room.getNumberOfResidents(), null);

            jdbcTemplate.update(insertObjReference, 4, objectId, room.getRoomType().getObjectId());
        } catch (NullPointerException e) {
            throw new TransactionException(room);
        }
        return objectId;
    }

    @Override
    public void updateRoom(Room newRoom, Room oldRoom) throws TransactionException {
        try {
            if (!oldRoom.getRoomNumber().equals(newRoom.getRoomNumber())) {
                jdbcTemplate.update(updateAttributes, newRoom.getRoomNumber(), null, newRoom.getObjectId(), 1);
            }
            if (!oldRoom.getNumberOfResidents().equals(newRoom.getNumberOfResidents())) {
                jdbcTemplate.update(updateAttributes, newRoom.getNumberOfResidents(), null, newRoom.getObjectId(), 2);
            }
            if (oldRoom.getRoomType().getObjectId() != newRoom.getRoomType().getObjectId()) {
                jdbcTemplate.update(updateReference, newRoom.getRoomType().getObjectId(), newRoom.getObjectId(), 4);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(newRoom);
        }
    }

    @Override
    public int deleteRoom(int id) {
        return deleteSingleEntityById(id);
    }
}
