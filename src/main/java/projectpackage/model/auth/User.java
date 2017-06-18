package projectpackage.model.auth;

import lombok.Data;
import projectpackage.model.notifications.Notification;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Set;

@Data
@ReactEntity(entityTypeName = "User")
@ReactReference(referenceName = "UserToNotificationAsAuthor", outerEntityClass = Notification.class, outerFieldName = "author", outerFieldKey = "objectId", innerFieldKey = "objectId", attrIdField = "21")
@ReactReference(referenceName = "UserToNotificationAsExecutor", outerEntityClass = Notification.class, outerFieldName = "executedBy", outerFieldKey = "objectId", innerFieldKey = "objectId", attrIdField = "24")
@ReactReference(referenceName = "UserToModificationHistory", outerEntityClass = ModificationHistory.class, outerFieldName = "modifAuthor", outerFieldKey = "objectId", innerFieldKey = "objectId")
@ReactReference(referenceName = "UserToOrderAsClient", outerEntityClass = Order.class, outerFieldName = "client",
        outerFieldKey = "objectId", innerFieldKey = "objectId", attrIdField = "7")
@ReactReference(referenceName = "UserToOrderAsLastModificator", outerEntityClass = Order.class, outerFieldName = "lastModificator",
        outerFieldKey = "objectId", innerFieldKey = "objectId", attrIdField = "44")
public class User implements ReactEntityWithId, Cloneable {

    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 15)
    private String email;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 16)
    private String password;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 17)
    private String firstName;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 18)
    private String lastName;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 19)
    private String additionalInfo;
    @ReactAttrField(valueObjectClass = Boolean.class, databaseAttrtypeIdValue = 3)
    private Boolean enabled;

    private Role role;
    private Set<Phone> phones;

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
