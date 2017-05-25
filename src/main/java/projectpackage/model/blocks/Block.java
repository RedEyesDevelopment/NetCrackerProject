package projectpackage.model.blocks;

import lombok.Data;
import projectpackage.model.rooms.Room;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;

@Data
@ReactEntity(entityTypeName = "Block")
public class Block implements ReactEntityWithId, Cloneable {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Block_start_date")
    private Date blockStartDate;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Block_finish_date")
    private Date blockFinishDate;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Reason")
    private String reason;

    private Room room;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
