package projectpackage.service.roomservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.OrderDTO;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.ratesdao.RateDAO;
import projectpackage.repository.roomsdao.RoomTypeDAO;
import projectpackage.service.orderservice.CategoryService;
import projectpackage.service.rateservice.PriceService;
import projectpackage.service.support.ServiceUtils;

import java.util.*;

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

    @Autowired
    RateDAO rateDAO;

    @Autowired
    PriceService priceService;

    @Transactional(readOnly = true)
    @Override
    public List<OrderDTO> getRoomTypes(Date startDate, Date finishDate, int numberOfPeople, int categoryId) {
        List<OrderDTO> list = new ArrayList<>();
        if (startDate == null || finishDate == null) {
            return list;
        }
        boolean isValidDates = serviceUtils.checkDates(startDate, finishDate);
        if (!isValidDates) {
            return list;
        }
        Set<Integer> availableRoomTypes = roomTypeDAO.getAvailableRoomTypes(numberOfPeople, new java.sql.Date(startDate.getTime()), new java.sql.Date(finishDate.getTime()));
        List<RoomType> allRoomTypes = getAllRoomTypes();
        for (RoomType roomType : allRoomTypes) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setRoomTypeName(roomType.getRoomTypeTitle());
            orderDTO.setRoomTypeDescription(roomType.getContent());
            Long categoryPrice = categoryService.getSingleCategoryById(categoryId).getCategoryPrice();
            Long days = (finishDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
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

    @Transactional(readOnly = true)
    @Override
    public Long getLivingCost(Date startDate, Date finishDate, int numberOfPeople, RoomType roomType) {
        return roomTypeDAO.getCostForLiving(roomType, numberOfPeople, startDate, finishDate);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoomType> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeDAO.getAllRoomTypes();
        if (roomTypes == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return roomTypes;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoomType> getSimpleRoomTypeList() {
        return roomTypeDAO.getSimpleRoomTypeList();
    }

    @Transactional(readOnly = true)
    @Override
    public RoomType getSingleRoomTypeById(Integer id) {
        RoomType roomType = roomTypeDAO.getRoomType(id);
        if (roomType == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return roomType;
    }

    @Transactional
    @Override
    public IUDAnswer deleteRoomType(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        roomTypeDAO.deleteRoomType(id);

        return new IUDAnswer(id, true);
    }

    @Transactional
    @Override
    public IUDAnswer insertRoomType(RoomType roomType) {
        if (roomType == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        Integer roomTypeId = roomTypeDAO.insertRoomType(roomType);
        Rate rate = getNewRate(roomTypeId);
        rateDAO.insertRate(rate);

        return new IUDAnswer(roomTypeId,true);
    }

    private Rate getNewRate(int roomTypeId) {
        Price price1 = new Price();
        price1.setRate(10_000L);
        price1.setNumberOfPeople(1);

        Price price2 = new Price();
        price2.setRate(10_000L);
        price2.setNumberOfPeople(2);

        Price price3 = new Price();
        price3.setRate(10_000L);
        price3.setNumberOfPeople(3);

        Set<Price> prices = new HashSet<>();
        prices.add(price1);
        prices.add(price2);
        prices.add(price3);

        Rate rate = new Rate();
        rate.setRateFromDate(new DateTime().minusYears(1).toDate());
        rate.setRateToDate(new DateTime().plusYears(2).toDate());
        rate.setRoomTypeId(roomTypeId);
        rate.setPrices(prices);

        return rate;
    }

    @Transactional
    @Override
    public IUDAnswer updateRoomType(Integer id, RoomType newRoomType) {
        if (newRoomType == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        newRoomType.setObjectId(id);
        RoomType oldRoomType = roomTypeDAO.getRoomType(id);
        roomTypeDAO.updateRoomType(newRoomType, oldRoomType);

        return new IUDAnswer(id,true);
    }

}
