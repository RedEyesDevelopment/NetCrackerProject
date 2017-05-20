package projectpackage.service.roomservice;

import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomServiceImpl implements RoomService{
    private static final Logger LOGGER = Logger.getLogger(RoomServiceImpl.class);

    @Autowired
    ReactEAVManager manager;

    @Autowired
    RoomDAO roomDAO;

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
        List<Room> rooms = null;
        try {
             rooms = manager.createReactEAV(Room.class).fetchInnerEntityCollection(RoomType.class).closeFetch().getEntityCollection();
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
        return rooms;
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
        int count = roomDAO.deleteRoom(id);
        LOGGER.info("Deleted rows : " + count);
        if (count == 0) return false;
        return true;
    }

    @Override
    public boolean insertRoom(Room room) {
        try {
            int roomId = roomDAO.insertRoom(room);
            LOGGER.info("Get from DB roomId = " + roomId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateRoom(int id, Room newRoom) {
        try {
            newRoom.setObjectId(id);
            Room oldRoom = (Room) manager.createReactEAV(Room.class).fetchInnerEntityCollection(RoomType.class).closeFetch()
                    .fetchInnerEntityCollection(Rate.class).closeFetch().fetchInnerEntityCollection(Price.class).closeFetch()
                    .getSingleEntityWithId(id);

            roomDAO.updateRoom(newRoom, oldRoom);
        } catch (ResultEntityNullException e) {
            LOGGER.warn("Problem with ReactEAV! Pls Check!", e);
            return false;
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
