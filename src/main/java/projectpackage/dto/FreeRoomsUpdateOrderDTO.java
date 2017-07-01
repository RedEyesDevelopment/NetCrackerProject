package projectpackage.dto;

import lombok.Data;
import projectpackage.model.rooms.Room;

import java.util.List;

/**
 * Created by Arizel on 01.07.2017.
 */
@Data
public class FreeRoomsUpdateOrderDTO {
    private List<Room> rooms;
    private Long livingCost;
    private Long categoryCost;
    private Long total;
}
