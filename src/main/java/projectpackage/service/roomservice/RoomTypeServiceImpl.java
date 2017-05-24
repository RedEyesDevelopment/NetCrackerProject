package projectpackage.service.roomservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.orders.Category;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.model.support.IUDAnswer;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.roomsdao.RoomTypeDAO;
import projectpackage.service.orderservice.CategoryService;

import java.util.*;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
public class RoomTypeServiceImpl implements RoomTypeService{
    private static final Logger LOGGER = Logger.getLogger(RoomTypeServiceImpl.class);

    @Autowired
    RoomTypeDAO roomTypeDAO;

    @Autowired
    CategoryService categoryService;

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
    public List<Map<String, Object>> getRoomTypes(Date startDate, Date finishDate, int numberOfPeople, int categoryId) {
        List<Map<String, Object>> answer = new ArrayList<Map<String, Object>>();
        List<RoomType> allRoomTypes = getAllRoomTypes();
        for (RoomType roomType : allRoomTypes) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("roomTypeTitle", roomType.getRoomTypeTitle());
            map.put("content", roomType.getContent());
            long categotyPrice = categoryService.getSingleCategoryById(categoryId).getCategoryPrice();
            long days = (finishDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
            Long categoryCost = categotyPrice * days;
            map.put(" ", categoryCost);
            // todo тут ещё много чего надо доделать

        }
        return answer;
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
        return null;
    }

    @Override
    public IUDAnswer deleteRoomType(int id) {
        try {
            roomTypeDAO.deleteRoomType(id);
        } catch (ReferenceBreakException e) {
            return new IUDAnswer(false, e.printReferencesEntities());
        }
        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer insertRoomType(RoomType roomType) {
        try {
            int roomTypeId = roomTypeDAO.insertRoomType(roomType);
            LOGGER.info("Get from DB roomId = " + roomTypeId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(false, e.getMessage());
        }
        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer updateRoomType(int id, RoomType newRoomType) {
        try {
            newRoomType.setObjectId(id);
            RoomType oldRoomType = roomTypeDAO.getRoomType(id);
            roomTypeDAO.updateRoomType(newRoomType, oldRoomType);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(false, e.getMessage());
        }
        return new IUDAnswer(true);
    }
}
