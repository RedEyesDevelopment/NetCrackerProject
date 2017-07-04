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
@ReactEntity(entityTypeId = 3)
@ReactReference(referenceName = "UserToNotificationAsAuthor", outerEntityClass = Notification.class, outerFieldName = "author", attrIdField = "21")
@ReactReference(referenceName = "UserToNotificationAsExecutor", outerEntityClass = Notification.class, outerFieldName = "executedBy", attrIdField = "24")
@ReactReference(referenceName = "UserToModificationHistory", outerEntityClass = ModificationHistory.class, outerFieldName = "modifAuthor")
@ReactReference(referenceName = "UserToOrderAsClient", outerEntityClass = Order.class, outerFieldName = "client", attrIdField = "7")
@ReactReference(referenceName = "UserToOrderAsLastModificator", outerEntityClass = Order.class, outerFieldName = "lastModificator", attrIdField = "44")
public class User implements ReactEntityWithId {

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

}
