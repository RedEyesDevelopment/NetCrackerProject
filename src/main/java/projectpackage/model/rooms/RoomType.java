package projectpackage.model.rooms;

import lombok.Data;
import projectpackage.model.rates.Rate;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Set;

@Data
@ReactEntity(entityTypeName = "Room_type")
@ReactReference(referenceName = "RoomTypeToRoom", outerEntityClass = Room.class, outerFieldName = "roomType", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class RoomType implements ReactEntityWithId {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Room_type_title")
    private String roomTypeTitle;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Content")
    private String content;

    private Set<Rate> rates;
}
