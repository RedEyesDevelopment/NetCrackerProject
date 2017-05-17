package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.Room;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomDAOImpl extends AbstractDAO implements RoomDAO{
    @Override
    public int insertRoom(Room room) throws TransactionException {
        return 0;
    }

    @Override
    public void updateRoom(Room newRoom, Room oldRoom) throws TransactionException {

    }
}
