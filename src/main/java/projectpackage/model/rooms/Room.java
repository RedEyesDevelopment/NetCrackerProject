package projectpackage.model.rooms;

import lombok.Data;
import projectpackage.model.blocks.Block;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeId = 1)
@ReactReference(referenceName = "RoomToBlock", outerEntityClass = Block.class, outerFieldName = "room")
@ReactReference(referenceName = "RoomToOrder", outerEntityClass = Order.class, outerFieldName = "room")
public class Room implements ReactEntityWithId {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = 1)
    private Integer roomNumber;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = 2)
    private Integer numberOfResidents;

    private RoomType roomType;

}
