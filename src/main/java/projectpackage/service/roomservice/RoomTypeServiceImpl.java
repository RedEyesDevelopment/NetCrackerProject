//package projectpackage.service.roomservice;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import projectpackage.model.rates.Price;
//import projectpackage.model.rates.Rate;
//import projectpackage.model.rooms.RoomType;
//import projectpackage.repository.daoexceptions.TransactionException;
//import projectpackage.repository.reacteav.ReactEAVManager;
//import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
//import projectpackage.repository.roomsdao.RoomTypeDAO;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by Arizel on 16.05.2017.
// */
//public class RoomTypeServiceImpl implements RoomTypeService{
//    private static final Logger LOGGER = Logger.getLogger(RoomTypeServiceImpl.class);
//
//    @Autowired
//    RoomTypeDAO roomTypeDAO;
//
//    @Autowired
//    ReactEAVManager manager;
//
//    @Override
//    public List<RoomType> getRoomTypes(Date date) {
//        return null;
//    }
//
//    @Override
//    public List<RoomType> getRoomTypes(Date date, long maxRate) {
//        return null;
//    }
//
//    @Override
//    public List<RoomType> getRoomTypes(Date date, int numberOfPeople) {
//        return null;
//    }
//
//    @Override
//    public List<RoomType> getRoomTypes(Date date, long maxRate, int numberOfPeople) {
//        return null;
//    }
//
//    @Override
//    public List<RoomType> getAllRoomTypes() {
//        return null;
//    }
//
//    @Override
//    public List<RoomType> getAllRoomTypes(String orderingParameter, boolean ascend) {
//        return null;
//    }
//
//    @Override
//    public RoomType getSingleRoomTypeById(int id) {
//        return null;
//    }
//
//    @Override
//    public boolean deleteRoomType(int id) {
//        int count = roomTypeDAO.deleteRoomType(id);
//        LOGGER.info("Deleted rows : " + count);
//        if (count == 0) return false;
//        return true;
//    }
//
//    @Override
//    public boolean insertRoomType(RoomType roomType) {
//        try {
//            int roomTypeId = roomTypeDAO.insertRoomType(roomType);
//            LOGGER.info("Get from DB roomId = " + roomTypeId);
//        } catch (TransactionException e) {
//            LOGGER.warn("Catched transactionException!!!", e);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean updateRoomType(int id, RoomType newRoomType) {
//        try {
//            newRoomType.setObjectId(id);
//            RoomType oldRoomType = (RoomType) manager.createReactEAV(RoomType.class).fetchInnerEntityCollection(Rate.class).closeFetch()
//                    .fetchInnerEntityCollection(Price.class).closeFetch().getSingleEntityWithId(id);
//
//            roomTypeDAO.updateRoomType(newRoomType, oldRoomType);
//        } catch (ResultEntityNullException e) {
//            LOGGER.warn("Problem with ReactEAV! Pls Check!", e);
//            return false;
//        } catch (TransactionException e) {
//            LOGGER.warn("Catched transactionException!!!", e);
//            return false;
//        }
//        return true;
//    }
//}
