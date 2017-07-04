package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactChild;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

@Data
@ReactEntity(entityTypeId = 9)
@ReactChild(outerEntityClass = User.class, outerFieldName = "phones", innerFieldKey = "userId")
public class Phone implements ReactEntityWithId {

    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%PARENT_ID")
    private int userId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 38)
    private String phoneNumber;

}
