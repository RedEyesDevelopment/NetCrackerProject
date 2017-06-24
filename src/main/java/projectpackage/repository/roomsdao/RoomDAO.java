package projectpackage.repository.roomsdao;

import projectpackage.model.rooms.Room;

import java.util.Date;
import java.util.List;

public interface RoomDAO {
    public Room getRoom(Integer id);
    public List<Room> getAllRooms();
    public Room getFreeRoom(int roomTypeId, int numberOfResidents, Date start, Date finish);
    public List<Room> getFreeRooms(int roomTypeId, int numberOfResidents, Date start, Date finish);
    public List<Room> getBookedRooms(int roomTypeId, int numberOfResidents, Date start, Date finish);
    public Integer insertRoom(Room room);
    public Integer updateRoom(Room newRoom, Room oldRoom);
    public void deleteRoom(Integer id);
}
