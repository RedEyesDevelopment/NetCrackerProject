package projectpackage.model.blocks;

import lombok.Data;
import projectpackage.model.rooms.Room;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;

@Data
@ReactEntity(entityTypeName = "Block")
public class Block implements ReactEntityWithId, Cloneable {
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Block_start_date")
    private Date blockStartDate;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Block_finish_date")
    private Date blockFinishDate;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = "Reason")
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
