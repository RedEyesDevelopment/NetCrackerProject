package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeName = "Phone")
@ReactChild(outerEntityClass = User.class, outerFieldName = "phones", outerFieldKey = "objectId", innerFieldKey = "userId")
public class Phone implements ReactEntityWithId, Cloneable {

    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%PARENT_ID")
    private int userId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = "Phone_number")
    private String phoneNumber;

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
