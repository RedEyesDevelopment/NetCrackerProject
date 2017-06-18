package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;

@Data
@ReactEntity(entityTypeName = "Modification_history")
@ReactChild(outerEntityClass = Order.class, outerFieldName = "historys", outerFieldKey = "objectId", innerFieldKey = "savedOrder")
public class ModificationHistory implements ReactEntityWithId, Cloneable {
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Modif_date")
    private Date modifiDate;

    private User modifAuthor;
    private Order savedOrder;

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
