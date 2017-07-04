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
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
@ReactEntity(entityTypeId = 6)
@ReactChild(outerEntityClass = RoomType.class, outerFieldName = "rates", innerFieldKey = "roomTypeId")
public class Rate implements ReactEntityWithId, Cloneable{
    private static final Logger LOGGER = Logger.getLogger(Rate.class.getName());

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
    public Rate clone() {
        try {
            return (Rate) super.clone();
        } catch (CloneNotSupportedException e) {
            LOGGER.log(Level.SEVERE, "Cannot clone due to unknown reason, check the model class!", e);
        }
        return null;
    }
}
