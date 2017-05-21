package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.annotations.ReactReference;

import java.util.Set;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeName = "Category")
@ReactReference(referenceName = "OrderToCategory", outerEntityClass = Order.class, outerFieldName = "category",
        outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Category {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;

    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Category_title")
    private String categoryTitle;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Category_price")
    private String categoryPrice;

    private Set<Complimentary> complimentaries;
}