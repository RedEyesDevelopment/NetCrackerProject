package projectpackage.model.rates;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;

@Data
@ReactEntity(entityTypeName = "Price")
@ReactChild(outerEntityClass = Rate.class, outerFieldName = "prices", outerFieldKey = "objectId", innerFieldKey = "rateId")
public class Price {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "Number_of_people")
    private Integer numberOfPeople;
    @ReactField(valueObjectClass = Long.class, databaseAttrtypeCodeValue = "Rate")
    private Long rate;
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%PARENT_ID")
    private int rateId;
}
