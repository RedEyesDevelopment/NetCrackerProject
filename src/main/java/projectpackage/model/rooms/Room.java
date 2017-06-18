package projectpackage.model.rooms;

import lombok.Data;
import projectpackage.model.blocks.Block;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeName = "Room")
@ReactReference(referenceName = "RoomToBlock", outerEntityClass = Block.class, outerFieldName = "room", outerFieldKey = "objectId", innerFieldKey = "objectId")
@ReactReference(referenceName = "RoomToOrder", outerEntityClass = Order.class, outerFieldName = "room", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Room implements ReactEntityWithId, Cloneable {
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "Room_number")
    private Integer roomNumber;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "Number_of_residents")
    private Integer numberOfResidents;

    private RoomType roomType;

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
