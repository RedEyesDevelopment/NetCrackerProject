package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.rooms.RoomType;
import projectpackage.service.roomservice.RoomService;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomRepositoryTests extends AbstractDatabaseTest{
    @Autowired
    RoomService roomService;

    @Test
    @Rollback(true)
    public void getRoomsByNumberOfResidents(int count){

    }

    @Test
    @Rollback(true)
    public void getRoomsByType(RoomType roomType) {

    }

    @Test
    @Rollback(true)
    public void getAllRooms() {

    }

    @Test
    @Rollback(true)
    public void getAllRooms(String orderingParameter, boolean ascend) {

    }

    @Test
    @Rollback(true)
    public void getSingleRoomById(){

    }


    @Test
    @Rollback(true)
    public void deleteRoom(){

    }

    @Test
    @Rollback(true)
    public void createRoom(){

    }

    @Test
    @Rollback(true)
    public void updateRoom(){

    }
}
