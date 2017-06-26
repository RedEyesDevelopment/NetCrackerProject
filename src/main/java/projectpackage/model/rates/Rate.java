package projectpackage.model.rates;

import lombok.Data;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;
import java.util.Set;

@Data
@ReactEntity(entityTypeId = 6)
@ReactChild(outerEntityClass = RoomType.class, outerFieldName = "rates", innerFieldKey = "roomTypeId")
public class Rate implements ReactEntityWithId, Cloneable{
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 30)
    private Date rateFromDate;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 31)
    private Date rateToDate;
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%PARENT_ID")
    private Integer roomTypeId;

    private Set<Price> prices;

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
