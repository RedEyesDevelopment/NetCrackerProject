package projectpackage.model.rates;

import lombok.Data;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;
import java.util.Set;

@Data
@ReactEntity(entityTypeName = "Rate")
@ReactChild(outerEntityClass = RoomType.class, outerFieldName = "rates", outerFieldKey = "objectId", innerFieldKey = "roomTypeId")
public class Rate implements ReactEntityWithId, Cloneable{
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Rate_from_date")
    private Date rateFromDate;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Rate_to_date")
    private Date rateToDate;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%PARENT_ID")
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
