package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.Phone;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.roomservice.RoomTypeService;

import java.util.Date;
import java.util.List;

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
        List<RoomType> roomTypes = roomTypeService.getAllRoomTypes();
        for (RoomType roomType : roomTypes) {
            LOGGER.info(roomType);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getAllRoomTypesWithArgs() {

    }

    @Test
    @Rollback(true)
    public void getSingleRoomTypeById(){
        RoomType roomType = roomTypeService.getSingleRoomTypeById(532);
        LOGGER.info(roomType);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void deleteRoomType(){
        int roomTypeId = 2075;
        IUDAnswer iudAnswer = roomTypeService.deleteRoomType(roomTypeId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete roomType result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createRoomType(){
        RoomType roomType = new RoomType();
        roomType.setContent("someContent");
        roomType.setRoomTypeTitle("Type epta");
        IUDAnswer iudAnswer = roomTypeService.insertRoomType(roomType);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create roomType result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateRoomType(){
        RoomType roomType = new RoomType();
        roomType.setObjectId(2011);
        roomType.setContent("new someContent");
        roomType.setRoomTypeTitle("new Type epta");
        IUDAnswer iudAnswer = roomTypeService.updateRoomType(2075, roomType);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update roomType result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
