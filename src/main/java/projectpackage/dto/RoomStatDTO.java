package projectpackage.dto;

import lombok.Data;
import projectpackage.model.rooms.Room;

import java.util.List;

@Data
public class RoomStatDTO {
    private List<Room> freeRooms;
    private List<Room> bookedRooms;
}
