package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.Room;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.Date;
import java.util.List;

public interface RoomDAO extends Commitable, Rollbackable {
    public Room getRoom(Integer id);
    public Room getSimpleRoom(Integer id);
    public List<Room> getAllRooms();
    public List<Room> getSimpleRoomsList();
    public Room getFreeRoom(int roomTypeId, int numberOfResidents, Date start, Date finish);
    public List<Room> getFreeRooms(int roomTypeId, int numberOfResidents, Date start, Date finish);
    public List<Room> getBookedRooms(int roomTypeId, int numberOfResidents, Date start, Date finish);
    public Integer insertRoom(Room room);
    public Integer updateRoom(Room newRoom, Room oldRoom);
    public void deleteRoom(Integer id);
}
