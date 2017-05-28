package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.roomservice.RoomService;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
public class RoomRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(RoomRepositoryTests.class);

    @Autowired
    RoomService roomService;

    @Test
    @Rollback(true)
    public void crudRoomTest() {
        RoomType insertRoomType = new RoomType();
        insertRoomType.setObjectId(8);
        Rate insertRate = new Rate();
        insertRate.setObjectId(43);
        Price insertPrice = new Price();
        insertPrice.setObjectId(91);
        Room insertRoom = new Room();
        insertRoom.setRoomNumber(111);
        insertRoom.setNumberOfResidents(1);
        insertRoom.setRoomType(insertRoomType);
        IUDAnswer insertAnswer = roomService.insertRoom(insertRoom);
        assertTrue(insertAnswer.isSuccessful());
        LOGGER.info("Create room result = " + insertAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        int roomId = insertAnswer.getObjectId();
        insertRoom.setObjectId(roomId);
        Room insertedRoom = roomService.getSingleRoomById(roomId);
        assertEquals(insertRoom, insertedRoom);

        RoomType updateRoomType = new RoomType();
        Room updateRoom = new Room();
        Rate updateRate = new Rate();
        Price updatePrice = new Price();
        updateRoom.setRoomNumber(112);
        updateRoom.setNumberOfResidents(2);
        updateRoomType.setObjectId(7);
        updateRoom.setRoomType(updateRoomType);
        updateRate.setObjectId(39);
        updatePrice.setObjectId(79);
        IUDAnswer iudAnswer = roomService.updateRoom(roomId, updateRoom);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update room result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Room updatedRoom = roomService.getSingleRoomById(roomId);
        assertEquals(updateRoom, updatedRoom);

        IUDAnswer deleteAnswer = roomService.deleteRoom(roomId);
        assertTrue(deleteAnswer.isSuccessful());
        LOGGER.info("Delete room result = " + deleteAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Room deletedRoom = roomService.getSingleRoomById(roomId);
        assertNull(deletedRoom);
    }

    @Test
    @Rollback(true)
    public void getRoomsByNumberOfResidents(){

    }

    @Test
    @Rollback(true)
    public void getRoomsByType() {

    }

    @Test
    @Rollback(true)
    public void getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        for (Room room:rooms){
            LOGGER.info(room);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleRoomById(){
        Room room = roomService.getSingleRoomById(534);
        LOGGER.info(room);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteRoom(){
        int roomId = 2074;
        IUDAnswer iudAnswer = roomService.deleteRoom(roomId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete room result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createRoom(){
        RoomType roomType = new RoomType();
        Room room = new Room();
        room.setRoomNumber(111);
        room.setNumberOfResidents(1);
        roomType.setObjectId(8);
        room.setRoomType(roomType);
        IUDAnswer iudAnswer = roomService.insertRoom(room);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create room result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateRoom(){
        RoomType roomType = new RoomType();
        Room room = new Room();
        room.setRoomNumber(112);
        room.setNumberOfResidents(2);
        roomType.setObjectId(7);
        room.setRoomType(roomType);
        IUDAnswer iudAnswer = roomService.updateRoom(2074, room);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update room result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
