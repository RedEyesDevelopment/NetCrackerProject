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
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.orderservice.CategoryService;

import java.util.Date;
import java.util.ArrayList;
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
    public List<OrderDTO> getRoomTypes(Date startDate, Date finishDate, int numberOfPeople, int categoryId) {
        List<OrderDTO> list = new ArrayList<>();
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
            //LOGGER.info( + "****************************************************");
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
            LOGGER.warn("Entity has references on self", e);
            return new IUDAnswer(id,false, e.printReferencesEntities());
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
    public IUDAnswer insertRoomType(RoomType roomType) {
        Integer roomTypeId = null;
        try {
            roomTypeId = roomTypeDAO.insertRoomType(roomType);
            LOGGER.info("Get from DB roomId = " + roomTypeId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(roomTypeId,false, "transactionInterrupt");
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
            return new IUDAnswer(id,false, "transactionInterrupt");
        }
        return new IUDAnswer(id,true);
    }
}
