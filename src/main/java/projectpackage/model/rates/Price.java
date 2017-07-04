package projectpackage.model.rates;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeId = 7)
@ReactChild(outerEntityClass = Rate.class, outerFieldName = "prices", innerFieldKey = "rateId")
public class Price implements ReactEntityWithId {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = 32)
    private Integer numberOfPeople;
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = 33)
    private Long rate;
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%PARENT_ID")
    private int rateId;

}
