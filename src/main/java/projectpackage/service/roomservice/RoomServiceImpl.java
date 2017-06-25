package projectpackage.service.roomservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.RoomDTO;
import projectpackage.dto.RoomStatDTO;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.roomsdao.RoomDAO;
import projectpackage.repository.roomsdao.RoomTypeDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
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
    public Room getSingleRoomById(Integer id) {
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
    public IUDAnswer deleteRoom(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            roomDAO.deleteRoom(id);
        } catch (ReferenceBreakException e) {
            return roomDAO.rollback(id, ON_ENTITY_REFERENCE, e);
        } catch (DeletedObjectNotExistsException e) {
            return roomDAO.rollback(id, DELETED_OBJECT_NOT_EXISTS, e);
        } catch (WrongEntityIdException e) {
            return roomDAO.rollback(id, WRONG_DELETED_ID, e);
        } catch (IllegalArgumentException e) {
            return roomDAO.rollback(id, NULL_ID, e);
        }
        roomDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertRoom(RoomDTO roomDTO) {
        if (roomDTO == null) return null;
        if (roomDTO.getNumberOfResidents() > 3 || roomDTO.getNumberOfResidents() < 1) {
            return new IUDAnswer(false, INCORRECT_NUMBER_OF_RESIDENTS);
        }
        Integer roomId = null;
        RoomType roomType = roomTypeDAO.getRoomType(roomDTO.getRoomType());
        Room room = new Room();
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setNumberOfResidents(roomDTO.getNumberOfResidents());
        room.setRoomType(roomType);
        try {
            roomId = roomDAO.insertRoom(room);
        } catch (IllegalArgumentException e) {
            return roomDAO.rollback(WRONG_FIELD, e);
        }
        roomDAO.commit();
        return new IUDAnswer(roomId, true);
    }

    @Override
    public IUDAnswer updateRoom(Integer id, RoomDTO roomDTO) {
        if (roomDTO == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        if (roomDTO.getNumberOfResidents() > 3 || roomDTO.getNumberOfResidents() < 1) {
            return new IUDAnswer(false, INCORRECT_NUMBER_OF_RESIDENTS);
        }
        RoomType roomType = roomTypeDAO.getRoomType(roomDTO.getRoomType());
        Room newRoom = new Room();
        newRoom.setObjectId(id);
        newRoom.setNumberOfResidents(roomDTO.getNumberOfResidents());
        newRoom.setRoomNumber(roomDTO.getRoomNumber());
        newRoom.setRoomType(roomType);
        Room oldRoom = roomDAO.getRoom(id);
        try {
            roomDAO.updateRoom(newRoom, oldRoom);
        } catch (IllegalArgumentException e) {
            return roomDAO.rollback(WRONG_FIELD, e);
        }
        roomDAO.commit();
        return new IUDAnswer(id, true);
    }
}
