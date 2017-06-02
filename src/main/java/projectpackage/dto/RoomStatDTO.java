package projectpackage.dto;

import lombok.Data;
import projectpackage.model.rooms.Room;

import java.util.List;

/**
 * Created by Arizel on 01.06.2017.
 */
@Data
public class RoomStatDTO {
    private List<Room> freeRooms;
    private List<Room> bookedRooms;
}
