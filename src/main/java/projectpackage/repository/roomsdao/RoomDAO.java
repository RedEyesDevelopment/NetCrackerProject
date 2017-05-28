package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.Room;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

public interface RoomDAO {
    public Room getRoom(Integer id);
    public List<Room> getAllRooms();
    public int insertRoom(Room room) throws TransactionException;
    public void updateRoom(Room newRoom, Room oldRoom) throws TransactionException;
    public void deleteRoom(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
