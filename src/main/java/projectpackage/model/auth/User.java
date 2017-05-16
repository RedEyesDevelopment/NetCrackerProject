package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Set;

@Data
@ReactEntity(entityTypeName = "User")
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
