package projectpackage.model.auth;

import lombok.Data;
import projectpackage.model.notifications.Notification;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Set;

@Data
@ReactEntity(entityTypeName = "User")
@ReactReference(outerEntityClass = Notification.class, outerFieldName = "author", outerFieldKey = "objectId", innerFieldKey = "objectId")
//@ReactReference(outerEntityClass = Notification.class, outerFieldName = "executedBy", outerFieldKey = "objectId", innerFieldKey = "objectId")
@ReactReference(outerEntityClass = ModificationHistory.class, outerFieldName = "modifAuthor", outerFieldKey = "objectId", innerFieldKey = "objectId")
@ReactReference(outerEntityClass = Order.class, outerFieldName = "client", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class User implements ReactEntityWithId {

    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Email")
    private String email;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Password")
    private String password;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "First_name")
    private String firstName;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Last_name")
    private String lastName;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Additional_info")
    private String additionalInfo;

    private Role role;
    private Set<Phone> phones;

}
