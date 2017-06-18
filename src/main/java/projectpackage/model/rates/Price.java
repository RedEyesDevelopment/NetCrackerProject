package projectpackage.model.rates;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeName = "Price")
@ReactChild(outerEntityClass = Rate.class, outerFieldName = "prices", outerFieldKey = "objectId", innerFieldKey = "rateId")
public class Price implements ReactEntityWithId, Cloneable {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = 32)
    private Integer numberOfPeople;
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = 33)
    private Long rate;
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%PARENT_ID")
    private int rateId;

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
