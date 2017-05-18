package projectpackage.repository.roomsdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomTypeDAOImpl extends AbstractDAO implements RoomTypeDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int insertRoomType(RoomType roomType) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObjects, objectId, null, 5, null, null);

            jdbcTemplate.update(insertAttributes, 28, objectId, roomType.getRoomTypeTitle(), null);
            jdbcTemplate.update(insertAttributes, 29, objectId, roomType.getContent(), null);
        } catch (NullPointerException e) {
            throw new TransactionException(roomType);
        }
        return objectId;
    }

    @Override
    public void updateRoomType(RoomType newRoomType, RoomType oldRoomType) throws TransactionException {
        try {
            if (!oldRoomType.getRoomTypeTitle().equals(newRoomType.getRoomTypeTitle())) {
                jdbcTemplate.update(updateAttributes, newRoomType.getRoomTypeTitle(), null, newRoomType.getObjectId(), 28);
            }
            if (!oldRoomType.getContent().equals(newRoomType.getContent())) {
                jdbcTemplate.update(updateAttributes, newRoomType.getContent(), null, newRoomType.getObjectId(), 29);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(newRoomType);
        }
    }

    @Override
    public int deleteRoomType(int id) {
        return deleteSingleEntityById(id);
    }
}
