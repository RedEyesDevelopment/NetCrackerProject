package projectpackage.service.roomservice;

import projectpackage.dto.RoomStatDTO;
import projectpackage.dto.RoomDTO;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RoomService extends MessageBook{
    public List<Room> getRoomsByNumberOfResidents(Integer count);
    public List<Room> getRoomsByType(RoomType roomType);
    public List<Room> getAllRooms();
    public Room getSingleRoomById(Integer id);
    public List<Room> getFreeRoomsOnPeriod(Date start, Date finish);
    public List<Room> getBookedRoomsOnPeriod(Date start, Date finish);
    public RoomStatDTO getAllRoomsOnPeriod(Date start, Date finish);
    public IUDAnswer deleteRoom(Integer id);
    public IUDAnswer insertRoom(RoomDTO roomDTO);
    public IUDAnswer updateRoom(Integer id, RoomDTO roomDTO);
}
