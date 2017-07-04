package projectpackage.dto;

import lombok.Data;
import projectpackage.model.rooms.Room;

import java.util.List;

@Data
public class FreeRoomsUpdateOrderDTO {
    private List<Room> rooms;
    private Long livingCost;
    private Long categoryCost;
    private Long total;
}
