package projectpackage.model.rooms;

import lombok.Data;
import projectpackage.model.rates.Rate;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Set;

@Data
@ReactEntity(entityTypeId = 5)
@ReactReference(referenceName = "RoomTypeToRoom", outerEntityClass = Room.class, outerFieldName = "roomType")
public class RoomType implements ReactEntityWithId, Cloneable {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 28)
    private String roomTypeTitle;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 29)
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
