package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Set;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeName = "Category")
@ReactReference(referenceName = "OrderToCategory", outerEntityClass = Order.class, outerFieldName = "category",
        outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Category implements ReactEntityWithId, Cloneable {
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;

    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = "Category_title")
    private String categoryTitle;
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = "Category_price")
    private Long categoryPrice;

    private Set<Complimentary> complimentaries;

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
