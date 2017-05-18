package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.service.roomservice.RoomService;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(RoomRepositoryTests.class);

    @Autowired
    RoomService roomService;

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
            System.out.println(room);
        }
    }

    @Test
    @Rollback(true)
    public void getSingleRoomById(){

    }


    @Test
    @Rollback(true)
    public void deleteRoom(){
        int roomId = 2010;
        boolean result = roomService.deleteRoom(roomId);
        assertTrue(result);
        LOGGER.info("Delete room result = " + result);
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
        boolean result = roomService.insertRoom(room);
        assertTrue(result);
        LOGGER.info("Create room result = " + result);
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
        boolean result = roomService.updateRoom(2010, room);
        assertTrue(result);
        LOGGER.info("Update room result = " + result);
        LOGGER.info(SEPARATOR);
    }
}
