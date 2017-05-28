package projectpackage.service.roomservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.rooms.RoomType;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.roomsdao.RoomTypeDAO;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
public class RoomTypeServiceImpl implements RoomTypeService{
    private static final Logger LOGGER = Logger.getLogger(RoomTypeServiceImpl.class);

    @Autowired
    RoomTypeDAO roomTypeDAO;

    @Override
    public List<RoomType> getRoomTypes(Date date) {
        return null;
    }

    @Override
    public List<RoomType> getRoomTypes(Date date, long maxRate) {
        return null;
    }

    @Override
    public List<RoomType> getRoomTypes(Date date, int numberOfPeople) {
        return null;
    }

    @Override
    public List<RoomType> getRoomTypes(Date date, long maxRate, int numberOfPeople) {
        return null;
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeDAO.getAllRoomTypes();
        if (roomTypes == null) LOGGER.info("Returned NULL!!!");
        return roomTypes;
    }

    @Override
    public List<RoomType> getAllRoomTypes(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public RoomType getSingleRoomTypeById(int id) {
        RoomType roomType = roomTypeDAO.getRoomType(id);
        if (roomType == null) LOGGER.info("Returned NULL!!!");
        return roomType;
    }

    @Override
    public IUDAnswer deleteRoomType(int id) {
        try {
            roomTypeDAO.deleteRoomType(id);
        } catch (ReferenceBreakException e) {
            return new IUDAnswer(id,false, e.printReferencesEntities());
        }
        return new IUDAnswer(id,true);
    }

    @Override
    public IUDAnswer insertRoomType(RoomType roomType) {
        Integer roomTypeId = null;
        try {
            roomTypeId = roomTypeDAO.insertRoomType(roomType);
            LOGGER.info("Get from DB roomId = " + roomTypeId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(roomTypeId,false, e.getMessage());
        }
        return new IUDAnswer(roomTypeId,true);
    }

    @Override
    public IUDAnswer updateRoomType(int id, RoomType newRoomType) {
        try {
            newRoomType.setObjectId(id);
            RoomType oldRoomType = roomTypeDAO.getRoomType(id);
            roomTypeDAO.updateRoomType(newRoomType, oldRoomType);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id,false, e.getMessage());
        }
        return new IUDAnswer(id,true);
    }
}
