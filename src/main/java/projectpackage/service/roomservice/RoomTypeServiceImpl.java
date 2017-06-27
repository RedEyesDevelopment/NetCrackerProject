package projectpackage.service.roomservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.OrderDTO;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.roomsdao.RoomTypeDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.orderservice.CategoryService;
import projectpackage.service.support.ServiceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


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

    @Autowired
    ServiceUtils serviceUtils;

    @Override
    public List<OrderDTO> getRoomTypes(Date startDate, Date finishDate, int numberOfPeople, int categoryId) {
        List<OrderDTO> list = new ArrayList<>();
        if (startDate == null || finishDate == null) return list;
        boolean isValidDates = serviceUtils.checkDates(startDate, finishDate);
        if (!isValidDates) return list;
        Set<Integer> availableRoomTypes = roomTypeDAO.getAvailableRoomTypes(numberOfPeople, new java.sql.Date(startDate.getTime()), new java.sql.Date(finishDate.getTime()));
        List<RoomType> allRoomTypes = getAllRoomTypes();
        for (RoomType roomType : allRoomTypes) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setRoomTypeName(roomType.getRoomTypeTitle());
            orderDTO.setRoomTypeDescription(roomType.getContent());
            long categoryPrice = categoryService.getSingleCategoryById(categoryId).getCategoryPrice();
            long days = (finishDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
            Long categoryCost = categoryPrice * days;
            orderDTO.setCategoryCost(categoryCost);
            orderDTO.setLivingPersons(numberOfPeople);
            orderDTO.setRoomTypeId(roomType.getObjectId());
            orderDTO.setArrival(startDate);
            orderDTO.setDeparture(finishDate);
            orderDTO.setCategoryId(categoryId);
            boolean available = availableRoomTypes.contains(roomType.getObjectId());
            orderDTO.setAvailable(available);
            if (available) {
                orderDTO.setLivingCost(roomTypeDAO.getCostForLiving(roomType, numberOfPeople, startDate, finishDate));
            }

            list.add(orderDTO);
        }
        LOGGER.info(list);
        return list;
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeDAO.getAllRoomTypes();
        if (roomTypes == null) LOGGER.info("Returned NULL!!!");
        return roomTypes;
    }

    @Override
    public List<RoomType> getSimpleRoomTypeList() {
        return roomTypeDAO.getSimpleRoomTypeList();
    }

    @Override
    public RoomType getSingleRoomTypeById(Integer id) {
        RoomType roomType = roomTypeDAO.getRoomType(id);
        if (roomType == null) LOGGER.info("Returned NULL!!!");
        return roomType;
    }

    @Override
    public IUDAnswer deleteRoomType(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            roomTypeDAO.deleteRoomType(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage());
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new IUDAnswer(id, false, NULL_ID, e.getMessage());
        }
        roomTypeDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertRoomType(RoomType roomType) {
        if (roomType == null) return null;
        Integer roomTypeId = null;
        try {
            roomTypeId = roomTypeDAO.insertRoomType(roomType);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
        }
        roomTypeDAO.commit();
        return new IUDAnswer(roomTypeId,true);
    }

    @Override
    public IUDAnswer updateRoomType(Integer id, RoomType newRoomType) {
        if (newRoomType == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            newRoomType.setObjectId(id);
            RoomType oldRoomType = roomTypeDAO.getRoomType(id);
            roomTypeDAO.updateRoomType(newRoomType, oldRoomType);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(id,false, WRONG_FIELD, e.getMessage());
        }
        roomTypeDAO.commit();
        return new IUDAnswer(id,true);
    }
}
