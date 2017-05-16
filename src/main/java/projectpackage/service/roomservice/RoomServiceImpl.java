package projectpackage.service.roomservice;

import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomServiceImpl implements RoomService{
    @Override
    public List<Room> getRoomsByNumberOfResidents(int count) {
        return null;
    }

    @Override
    public List<Room> getRoomsByType(RoomType roomType) {
        return null;
    }

    @Override
    public List<Room> getAllRooms() {
        return null;
    }

    @Override
    public List<Room> getAllRooms(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public Room getSingleRoomById(int id) {
        return null;
    }

    @Override
    public boolean deleteRoom(Room room) {
        return false;
    }

    @Override
    public boolean insertRoom(Room room) {
        return false;
    }

    @Override
    public boolean updateRoom(Room newRoom) {
        return false;
    }
}
