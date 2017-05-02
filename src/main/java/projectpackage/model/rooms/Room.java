package projectpackage.model.rooms;

import lombok.Data;

@Data
public class Room {
    private Integer roomNumber;
    private RoomType roomType;
    private Integer numberOfResidents;
    private Double deltaPct;
}
