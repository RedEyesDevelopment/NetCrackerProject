package projectpackage.service.roomservice;

import projectpackage.dto.RoomStatDTO;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.dto.IUDAnswer;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RoomService {
    public List<Room> getRoomsByNumberOfResidents(int count);
    public List<Room> getRoomsByType(RoomType roomType);

    public List<Room> getAllRooms();
    public List<Room> getAllRooms(String orderingParameter, boolean ascend);
    public Room getSingleRoomById(int id);
    public List<Room> doesBlockedRoomOnDay(Room room, Date date);
    public List<Room> getFreeRoomsOnPeriod(Date start, Date finish);
    public List<Room> getBookedRoomsOnPeriod(Date start, Date finish);
    public RoomStatDTO getAllRoomsOnPeriod(Date start, Date finish);
    public IUDAnswer deleteRoom(int id);
    public IUDAnswer insertRoom(Room room);
    public IUDAnswer updateRoom(int id, Room newRoom);
}
