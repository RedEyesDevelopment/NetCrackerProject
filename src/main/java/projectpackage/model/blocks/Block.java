package projectpackage.model.blocks;

import lombok.Data;
import projectpackage.model.rooms.Room;

import java.util.Date;

@Data
public class Block {
    private Room room;
    private Date blockStartDate;
    private Date blockFinishDate;
    private String reason;
}
