package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.rooms.Room;
import projectpackage.service.roomservice.RoomService;

import java.util.List;

//import projectpackage.model.support.IUDAnswer;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
public class RoomRepositoryTests extends AbstractDatabaseTest {
    private static final Logger LOGGER = Logger.getLogger(RoomRepositoryTests.class);

    @Autowired
    RoomService roomService;

//    @Test
//    @Rollback(true)
//    public void crudRoomTest() {
//
//        RoomType insertRoomType = new RoomType();
//        insertRoomType.setObjectId(8);
//        insertRoomType.setRoomTypeTitle("President");
//        insertRoomType.setContent("President content");
//
//        Rate insertRate = new Rate();
//        insertRate.setObjectId(43);
//        insertRate.setRateFromDate(new Date(16000L));
//        insertRate.setRateToDate(new Date(16000L));
//        insertRate.setRoomTypeId(8);
//
//        Rate insertRate1 = new Rate();
//        insertRate1.setRateFromDate(new Date(16000L));
//        insertRate1.setRateToDate(new Date(16000L));
//        insertRate1.setRoomTypeId(8);
//
//        Rate insertRate2 = new Rate();
//        insertRate2.setObjectId(45);
//        insertRate2.setRateFromDate(new Date(16000L));
//        insertRate2.setRateToDate(new Date(16000L));
//        insertRate2.setRoomTypeId(8);
//
//        Rate insertRate3 = new Rate();
//        insertRate3.setObjectId(46);
//        insertRate3.setRateFromDate(new Date(16000L));
//        insertRate3.setRateToDate(new Date(16000L));
//        insertRate3.setRoomTypeId(8);
//
//        Set<Rate> rates = new HashSet<>();
//        rates.add(insertRate);
//        rates.add(insertRate1);
//        rates.add(insertRate2);
//        rates.add(insertRate3);
//
//        Price insertPrice = new Price();
//        insertPrice.setObjectId(91);
//        insertPrice.setNumberOfPeople(1);
//        insertPrice.setRate((long) 10000);
//
//        Price insertPrice1 = new Price();
//        insertPrice1.setObjectId(94);
//        insertPrice1.setNumberOfPeople(1);
//        insertPrice1.setRate((long) 10000);
//
//        Price insertPrice2 = new Price();
//        insertPrice2.setObjectId(97);
//        insertPrice2.setNumberOfPeople(1);
//        insertPrice2.setRate((long) 10000);
//
//        Price insertPrice3 = new Price();
//        insertPrice3.setObjectId(100);
//        insertPrice3.setNumberOfPeople(1);
//        insertPrice3.setRate((long) 1000);
//
//        Set<Price> prices = new HashSet<>();
//        prices.add(insertPrice);
//        prices.add(insertPrice1);
//        prices.add(insertPrice2);
//        prices.add(insertPrice3);
//
//        Room insertRoom = new Room();
//        insertRoom.setRoomNumber(111);
//        insertRoom.setNumberOfResidents(1);
//
//        insertRoom.setRoomType(insertRoomType);
//        insertRoomType.setRates(rates);
//        insertRate.setPrices(prices);
//        insertRate1.setPrices(prices);
//        insertRate2.setPrices(prices);
//        insertRate3.setPrices(prices);
//
//        IUDAnswer insertAnswer = roomService.insertRoom(insertRoom);
//        assertTrue(insertAnswer.isSuccessful());
//        LOGGER.info("Create room result = " + insertAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//
//        int roomId = insertAnswer.getObjectId();
//        insertRoom.setObjectId(roomId);
//        Room insertedRoom = roomService.getSingleRoomById(roomId);
//        assertEquals(insertRoom, insertedRoom);
//
//        RoomType updateRoomType = new RoomType();
//
//
//        insertRoomType.setObjectId(7);
//        insertRoomType.setRoomTypeTitle("Econom");
//        insertRoomType.setContent("Econom content");
//
//        Rate updateRate = new Rate();
//        updateRate.setObjectId(39);
//        updateRate.setRateFromDate(new Date(16000L));
//        updateRate.setRateToDate(new Date(16000L));
//        updateRate.setRoomTypeId(7);
//
//        Rate updateRate1 = new Rate();
//        updateRate1.setObjectId(40);
//        updateRate1.setRateFromDate(new Date(16000L));
//        updateRate1.setRateToDate(new Date(16000L));
//        updateRate1.setRoomTypeId(7);
//
//        Rate updateRate2 = new Rate();
//        updateRate2.setObjectId(41);
//        updateRate2.setRateFromDate(new Date(16000L));
//        updateRate2.setRateToDate(new Date(16000L));
//        updateRate2.setRoomTypeId(7);
//
//        Rate updateRate3 = new Rate();
//        updateRate3.setObjectId(42);
//        updateRate3.setRateFromDate(new Date(16000L));
//        updateRate3.setRateToDate(new Date(16000L));
//        updateRate3.setRoomTypeId(7);
//
//        Price updatePrice = new Price();
//        updatePrice.setObjectId(80);
//        updatePrice.setNumberOfPeople(1);
//        updatePrice.setRate((long) 10000);
//
//        Price updatePrice1 = new Price();
//        updatePrice1.setObjectId(83);
//        updatePrice1.setNumberOfPeople(1);
//        updatePrice1.setRate((long) 10000);
//
//        Price updatePrice2 = new Price();
//        updatePrice2.setObjectId(86);
//        updatePrice2.setNumberOfPeople(1);
//        updatePrice2.setRate((long) 10000);
//
//        Price updatePrice3 = new Price();
//        updatePrice3.setObjectId(89);
//        updatePrice3.setNumberOfPeople(1);
//        updatePrice3.setRate((long) 10000);
//
//        Room updateRoom = new Room();
//        updateRoom.setRoomNumber(112);
//        updateRoom.setNumberOfResidents(2);
//        updateRoomType.setObjectId(7);
//
//        updateRoom.setRoomType(updateRoomType);
//        updateRoomType.setRates(rates);
//        updateRate.setPrices(prices);
//        updateRate1.setPrices(prices);
//        updateRate2.setPrices(prices);
//        updateRate3.setPrices(prices);
//
//        IUDAnswer iudAnswer = roomService.updateRoom(roomId, updateRoom);
//        assertTrue(iudAnswer.isSuccessful());
//        LOGGER.info("Update room result = " + iudAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//
//        Room updatedRoom = roomService.getSingleRoomById(roomId);
//        assertEquals(updateRoom, updatedRoom);
//
//        IUDAnswer deleteAnswer = roomService.deleteRoom(roomId);
//        assertTrue(deleteAnswer.isSuccessful());
//        LOGGER.info("Delete room result = " + deleteAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//
//        Room deletedRoom = roomService.getSingleRoomById(roomId);
//        assertNull(deletedRoom);
//    }

    @Test
    @Rollback(true)
    public void getRoomsByNumberOfResidents() {

    }

    @Test
    @Rollback(true)
    public void getRoomsByType() {

    }

    @Test
    @Rollback(true)
    public void getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        for (Room room : rooms) {
            LOGGER.info(room);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleRoomById() {
        Room room = roomService.getSingleRoomById(534);
        LOGGER.info(room);
        LOGGER.info(SEPARATOR);
    }


//    @Test
//    @Rollback(true)
//    public void deleteRoom() {
//        int roomId = 2074;
//        IUDAnswer iudAnswer = roomService.deleteRoom(roomId);
//        assertTrue(iudAnswer.isSuccessful());
//        LOGGER.info("Delete room result = " + iudAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//    }

//    @Test
//    @Rollback(true)
//    public void createRoom() {
//        RoomType roomType = new RoomType();
//        Room room = new Room();
//        room.setRoomNumber(111);
//        room.setNumberOfResidents(1);
//        roomType.setObjectId(8);
//        room.setRoomType(roomType);
//        IUDAnswer iudAnswer = roomService.insertRoom(room);
//        assertTrue(iudAnswer.isSuccessful());
//        LOGGER.info("Create room result = " + iudAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//    }

//    @Test
//    @Rollback(true)
//    public void updateRoom() {
//        RoomType roomType = new RoomType();
//        Room room = new Room();
//        room.setRoomNumber(112);
//        room.setNumberOfResidents(2);
//        roomType.setObjectId(7);
//        room.setRoomType(roomType);
//        IUDAnswer iudAnswer = roomService.updateRoom(2074, room);
//        assertTrue(iudAnswer.isSuccessful());
//        LOGGER.info("Update room result = " + iudAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//    }

    @Test
    public void testSimpleList(){
        List<Room> rooms = roomService.getSimpleRoomList();
        LOGGER.info(rooms);
    }
}
