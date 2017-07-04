package projectpackage.service.roomservice;

import projectpackage.dto.RoomStatDTO;
import projectpackage.dto.RoomDTO;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.Date;
import java.util.List;

public interface RoomService extends MessageBook{
    List<Room> getRoomsByNumberOfResidents(Integer count);
    List<Room> getRoomsByType(RoomType roomType);
    List<Room> getAllRooms();
    List<Room> getSimpleRoomList();
    Room getSingleRoomById(Integer id);
    Room getSimpleRoomById(Integer id);
    Room getFreeRoom(int roomTypeId, int numberOfResidents, Date start, Date finish);
    List<Room> getFreeRooms(int roomTypeId, int numberOfResidents, Date start, Date finish);
    List<Room> getFreeRoomsOnPeriod(Date start, Date finish);
    List<Room> getBookedRoomsOnPeriod(Date start, Date finish);
    RoomStatDTO getAllRoomsOnPeriod(Date start, Date finish);
    IUDAnswer deleteRoom(Integer id);
    IUDAnswer insertRoom(RoomDTO roomDTO);
    IUDAnswer updateRoom(Integer id, RoomDTO roomDTO);
}
