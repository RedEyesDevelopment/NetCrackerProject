package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.Phone;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.service.roomservice.RoomTypeService;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomTypeRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(RoomTypeRepositoryTests.class);


    @Autowired
    RoomTypeService roomTypeService;

    @Test
    @Rollback(true)
    public void getRoomTypes(){

    }

    @Test
    @Rollback(true)
    public void getRoomTypesWithArgs(){

    }

    @Test
    @Rollback(true)
    public void getRoomTypesWithTwoArgs(){

    }

    @Test
    @Rollback(true)
    public void getRoomTypesWithThreeArgs() {

    }

    @Test
    @Rollback(true)
    public void getAllRoomTypes() {

    }

    @Test
    @Rollback(true)
    public void getAllRoomTypesWithArgs() {

    }

    @Test
    @Rollback(true)
    public void getSingleRoomTypeById(){

    }

    @Test
    @Rollback(true)
    public void deleteRoomType(){
        int roomTypeId = 2011;
        boolean result = roomTypeService.deleteRoomType(roomTypeId);
        assertTrue(result);
        LOGGER.info("Delete roomType result = " + result);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createRoomType(){
        RoomType roomType = new RoomType();
        roomType.setContent("someContent");
        roomType.setRoomTypeTitle("Type epta");
        boolean result = roomTypeService.insertRoomType(roomType);
        assertTrue(result);
        LOGGER.info("Create roomType result = " + result);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateRoomType(){
        RoomType roomType = new RoomType();
        roomType.setObjectId(2011);
        roomType.setContent("new someContent");
        roomType.setRoomTypeTitle("new Type epta");
        boolean result = roomTypeService.updateRoomType(2011, roomType);
        assertTrue(result);
        LOGGER.info("Update roomType result = " + result);
        LOGGER.info(SEPARATOR);
    }
}
