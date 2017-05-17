package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.rooms.Room;
import projectpackage.service.roomservice.RoomService;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RoomRepositoryTests extends AbstractDatabaseTest{
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
