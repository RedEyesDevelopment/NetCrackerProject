package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.service.roomservice.RoomTypeService;

import java.util.Date;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomTypeRepositoryTests extends AbstractDatabaseTest{
    @Autowired
    RoomTypeService roomTypeService;

    @Test
    @Rollback(true)
    public void getRoomTypes(Date date){

    }

    @Test
    @Rollback(true)
    public void getRoomTypes(Date date, long maxRate){

    }

    @Test
    @Rollback(true)
    public void getRoomTypes(Date date, int numberOfPeople){

    }

    @Test
    @Rollback(true)
    public void getRoomTypes(Date date, long maxRate, int numberOfPeople) {

    }

    @Test
    @Rollback(true)
    public void getAllRoomTypes() {

    }

    @Test
    @Rollback(true)
    public void getAllRoomTypes(String orderingParameter, boolean ascend) {

    }

    @Test
    @Rollback(true)
    public void getSingleRoomTypeById(){

    }

    @Test
    @Rollback(true)
    public void deleteRoomType(){

    }

    @Test
    @Rollback(true)
    public void createRoomType(){

    }

    @Test
    @Rollback(true)
    public void updateRoomType(){

    }
}
