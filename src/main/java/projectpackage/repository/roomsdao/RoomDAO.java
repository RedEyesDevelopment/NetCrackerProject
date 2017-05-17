package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.Room;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RoomDAO {
    public int insertRoom(Room room) throws TransactionException;
    public void updateRoom(Room newRoom, Room oldRoom) throws TransactionException;
    public int deleteRoom(int id);
}
