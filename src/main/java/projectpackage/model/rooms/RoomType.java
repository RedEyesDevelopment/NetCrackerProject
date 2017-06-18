package projectpackage.model.rooms;

import lombok.Data;
import projectpackage.model.rates.Rate;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Set;

@Data
@ReactEntity(entityTypeName = "Room_type")
@ReactReference(referenceName = "RoomTypeToRoom", outerEntityClass = Room.class, outerFieldName = "roomType", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class RoomType implements ReactEntityWithId, Cloneable {
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = "Room_type_title")
    private String roomTypeTitle;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = "Content")
    private String content;

    private Set<Rate> rates;

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
