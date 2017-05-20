package projectpackage.model.rates;

import lombok.Data;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;
import java.util.Set;

@Data
@ReactEntity(entityTypeName = "Rate")
@ReactChild(outerEntityClass = RoomType.class, outerFieldName = "rates", outerFieldKey = "objectId", innerFieldKey = "roomTypeId")
public class Rate implements ReactEntityWithId, Cloneable{
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Rate_from_date")
    private Date rateFromDate;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Rate_to_date")
    private Date rateToDate;
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%PARENT_ID")
    private int roomTypeId;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Creation_date")
    private Date creationDate;

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
