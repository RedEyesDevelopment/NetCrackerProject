package projectpackage.model.blocks;

import lombok.Data;
import projectpackage.model.rooms.Room;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;

@Data
@ReactEntity(entityTypeId = 8)
public class Block implements ReactEntityWithId {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 35)
    private Date blockStartDate;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 36)
    private Date blockFinishDate;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 37)
    private String reason;

    private Room room;

}
