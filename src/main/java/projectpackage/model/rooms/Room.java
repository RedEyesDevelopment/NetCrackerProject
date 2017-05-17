package projectpackage.model.rooms;

import lombok.Data;
import projectpackage.model.blocks.Block;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeName = "Room")
@ReactReference(outerEntityClass = Block.class, outerFieldName = "room", outerFieldKey = "objectId", innerFieldKey = "objectId")
@ReactReference(outerEntityClass = Order.class, outerFieldName = "room", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Room implements ReactEntityWithId {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "Room_number")
    private Integer roomNumber;
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "Number_of_residents")
    private Integer numberOfResidents;

    private RoomType roomType;

}
