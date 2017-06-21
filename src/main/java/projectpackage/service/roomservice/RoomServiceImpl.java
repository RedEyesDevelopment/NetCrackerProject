package projectpackage.service.roomservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.RoomStatDTO;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.roomsdao.RoomDAO;
import projectpackage.repository.roomsdao.RoomTypeDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
public class RoomServiceImpl implements RoomService {
    private static final Logger LOGGER = Logger.getLogger(RoomServiceImpl.class);

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    RoomTypeDAO roomTypeDAO;

    @Override
    public List<Room> getRoomsByNumberOfResidents(int count) {
        List<Room> answer = new ArrayList<Room>();
        List<Room> allRooms = getAllRooms();
        for (Room room : allRooms) {
            if (room.getNumberOfResidents().equals(count)) {
                answer.add(room);
            }
        }
        return answer;
    }

    @Override
    public List<Room> getRoomsByType(RoomType roomType) {
        List<Room> answer = new ArrayList<Room>();
        String roomTypeTitle = roomType.getRoomTypeTitle();
        List<Room> allRooms = getAllRooms();
        for (Room room : allRooms) {
            if (room.getRoomType().getRoomTypeTitle().equals(roomTypeTitle)) {
                answer.add(room);
            }
        }
        return answer;
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
    public List<Room> doesBlockedRoomOnDay(Room room, Date date) {
        return null;
    }

    @Override
    public List<Room> getFreeRoomsOnPeriod(Date start, Date finish) {
        List<Room> list = new ArrayList<>();
        List<Room> allRooms = getAllRooms();
        List<RoomType> roomTypes = roomTypeDAO.getAllRoomTypes();
        for (int i = 0; i < roomTypes.size(); i++) {
            RoomType roomType = roomTypes.get(i);
            for (int j = 1; j <= 3; j++) {
                list.addAll(roomDAO.getFreeRooms(roomType.getObjectId(), j, start, finish));
            }
        }
        return list;
    }

    @Override
    public List<Room> getBookedRoomsOnPeriod(Date start, Date finish) {
        List<Room> list = new ArrayList<>();
        List<Room> allRooms = getAllRooms();
        List<RoomType> roomTypes = roomTypeDAO.getAllRoomTypes();
        for (int i = 0; i < roomTypes.size(); i++) {
            RoomType roomType = roomTypes.get(i);
            for (int j = 1; j <= 3; j++) {
                list.addAll(roomDAO.getBookedRooms(roomType.getObjectId(), j, start, finish));
            }
        }
        return list;
    }

    @Override
    public RoomStatDTO getAllRoomsOnPeriod(Date start, Date finish) {
        RoomStatDTO roomStatDTO = new RoomStatDTO();
        roomStatDTO.setFreeRooms(getFreeRoomsOnPeriod(start, finish));
        roomStatDTO.setBookedRooms(getBookedRoomsOnPeriod(start, finish));
        return roomStatDTO;
    }


    @Override
    public IUDAnswer deleteRoom(int id) {
        try {
            roomDAO.deleteRoom(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn("Entity has references on self", e);
            return new IUDAnswer(id, false, e.printReferencesEntities());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn("Entity with that id does not exist!", e);
            return new IUDAnswer(id, "deletedObjectNotExists");
        } catch (WrongEntityIdException e) {
            LOGGER.warn("This id belong another entity class!", e);
            return new IUDAnswer(id, "wrongDeleteId");
        }
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertRoom(Room room) {
        if (room == null) return new IUDAnswer(false, "transactionInterrupt");
        if (room.getNumberOfResidents() > 3 || room.getNumberOfResidents() < 1) {
            return new IUDAnswer(false, "transactionInterrupt");
        }
        Integer roomId = null;
        try {
            roomId = roomDAO.insertRoom(room);
            LOGGER.info("Get from DB roomId = " + roomId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(roomId, false, "transactionInterrupt");
        }
        return new IUDAnswer(roomId, true);
    }

    @Override
    public IUDAnswer updateRoom(int id, Room newRoom) {
        if (newRoom == null) return new IUDAnswer(false, "transactionInterrupt");
        if (newRoom.getNumberOfResidents() > 3 || newRoom.getNumberOfResidents() < 1) {
            return new IUDAnswer(false, "transactionInterrupt");
        }
        try {
            newRoom.setObjectId(id);
            Room oldRoom = roomDAO.getRoom(id);
            roomDAO.updateRoom(newRoom, oldRoom);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id, false, "transactionInterrupt");
        }
        return new IUDAnswer(id, true);
    }
}
