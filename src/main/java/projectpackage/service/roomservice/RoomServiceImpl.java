package projectpackage.service.roomservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.model.support.IUDAnswer;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.roomsdao.RoomDAO;

import java.util.List;


/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
public class RoomServiceImpl implements RoomService{
    private static final Logger LOGGER = Logger.getLogger(RoomServiceImpl.class);

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
        List<Room> rooms = roomDAO.getAllRooms();
        if (rooms == null) LOGGER.info("Returned NULL!!!");
        return rooms;
    }

    @Override
    public List<Room> getAllRooms(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public Room getSingleRoomById(int id) {
        Room room = roomDAO.getRoom(id);
        if (room == null) LOGGER.info("Returned NULL!!!");
        return room;
    }

    @Override
    public IUDAnswer deleteRoom(int id) {
        try {
            roomDAO.deleteRoom(id);
        } catch (ReferenceBreakException e) {
            return new IUDAnswer(id,false, e.printReferencesEntities());
        }
        return new IUDAnswer(id,true);
    }

    @Override
    public IUDAnswer insertRoom(Room room) {
        Integer roomId = null;
        try {
            roomId = roomDAO.insertRoom(room);
            LOGGER.info("Get from DB roomId = " + roomId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(roomId,false, e.getMessage());
        }
        return new IUDAnswer(roomId,true);
    }

    @Override
    public IUDAnswer updateRoom(int id, Room newRoom) {
        try {
            newRoom.setObjectId(id);
            Room oldRoom = roomDAO.getRoom(id);
            roomDAO.updateRoom(newRoom, oldRoom);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id,false, e.getMessage());
        }
        return new IUDAnswer(id,true);
    }
}
