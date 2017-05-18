package projectpackage.service.roomservice;

import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.reacteav.ReactEAVManager;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomServiceImpl implements RoomService{

    @Autowired
    ReactEAVManager manager;

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
//        List<Room> rooms = null;
//        try {
//             rooms = manager.createReactEAV(Room.class).fetchInnerEntityCollection(RoomType.class).fetchInnerEntityCollectionForInnerObject(Rate.class).fetchInnerEntityCollectionForInnerObject(Price.class).closeFetch().getEntityCollection();
//        } catch (ResultEntityNullException e) {
//            e.printStackTrace();
//        }
//        return rooms;
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
    public boolean deleteRoom(int id) {
        return false;
    }

    @Override
    public boolean insertRoom(Room room) {
        return false;
    }

    @Override
    public boolean updateRoom(int id, Room newRoom) {
        return false;
    }
}
