package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.Room;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.Date;
import java.util.List;

public interface RoomDAO {
    public Room getRoom(Integer id);
    public List<Room> getAllRooms();
    public Room getFreeRoom(int roomTypeId, int numberOfResidents, Date start, Date finish);
    public List<Room> getFreeRooms(int roomTypeId, int numberOfResidents, Date start, Date finish);
    public List<Room> getBookedRooms(int roomTypeId, int numberOfResidents, Date start, Date finish);
    public Integer insertRoom(Room room) throws TransactionException;
    public Integer updateRoom(Room newRoom, Room oldRoom) throws TransactionException;
    public void deleteRoom(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
