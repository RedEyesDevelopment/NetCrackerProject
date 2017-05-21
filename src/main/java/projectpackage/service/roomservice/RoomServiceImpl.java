package projectpackage.service.roomservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.blocks.Block;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.blocksdao.BlockDAO;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.roomsdao.RoomDAO;

import java.util.List;


/**
 * Created by Arizel on 16.05.2017.
 */
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
    public boolean deleteRoom(int id) {
//        List<Block> blocks = blockDAO.getAllBlocks();
//        for (Block block : blocks) {
//            if (block.getRoom().getObjectId() == id) {
//                blockDAO.deleteBlock(block.getObjectId());
//            }
//        }

//        int count = roomDAO.deleteRoom(id);
//        LOGGER.info("Deleted rows : " + count);
//        if (count == 0) return false;
//        return true;

//        try {
//            roomDAO.deleteRoom(id);
//        } catch (ReferenceBreakException e) {
//            return new POJO(fal)
//        }
//        return new POJO()
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
            Room oldRoom = roomDAO.getRoom(id);
            roomDAO.updateRoom(newRoom, oldRoom);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
