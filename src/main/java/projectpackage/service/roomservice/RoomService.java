package projectpackage.service.roomservice;

import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RoomService {
    public List<Room> getRoomsByNumberOfResidents(int count);//TODO Denis
    public List<Room> getRoomsByType(RoomType roomType);//TODO Denis

    public List<Room> getAllRooms();//TODO Pacanu
    public List<Room> getAllRooms(String orderingParameter, boolean ascend);//TODO Pacanu
    public Room getSingleRoomById(int id);//TODO Pacanu
    public boolean deleteRoom(Room room);//TODO Pacanu
    public boolean insertRoom(Room room);//TODO Pacanu
    public boolean updateRoom(Room newRoom);//TODO Pacanu
}
