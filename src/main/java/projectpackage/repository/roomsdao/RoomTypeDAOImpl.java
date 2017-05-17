package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomTypeDAOImpl extends AbstractDAO implements RoomTypeDAO{
    @Override
    public int insertRoomType(RoomType roomType) throws TransactionException {
        return 0;
    }

    @Override
    public void updateRoomType(RoomType newRoomType, RoomType oldRoomType) throws TransactionException {

    }

    @Override
    public int deleteRoomType(int id) {
        return deleteSingleEntityById(id);
    }
}
