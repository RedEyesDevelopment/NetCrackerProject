package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.annotations.ReactEntity;
import projectpackage.repository.reacdao.annotations.ReactField;
import projectpackage.repository.reacdao.fetch.EntityOuterRelationshipsData;
import projectpackage.repository.reacdao.fetch.EntityReferenceRelationshipsData;
import projectpackage.repository.reacdao.fetch.EntityVariablesData;
import projectpackage.repository.reacdao.models.ReacEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

@Data
@ReactEntity(entityTypeName = "User")
public class User extends ReacEntity {
//    private static final String OBJECT_TYPE="User";
//    private static final LinkedHashMap<String, EntityVariablesData> objectProperties;
//    private static final HashMap<String, EntityOuterRelationshipsData> objectOuterConnections;
//    private static final HashMap<String, EntityReferenceRelationshipsData> objectReferenceConnections;
//
//    static
//    {
//        objectProperties = new LinkedHashMap<>();
//        objectProperties.put("objectId",new EntityVariablesData(Integer.class, "%OBJECT_ID"));
//        objectProperties.put("email",new EntityVariablesData(String.class, "Email"));
//        objectProperties.put("password",new EntityVariablesData(String.class, "Password"));
//        objectProperties.put("firstName",new EntityVariablesData(String.class, "First_name"));
//        objectProperties.put("lastName",new EntityVariablesData(String.class, "Last_name"));
//        objectProperties.put("additionalInfo",new EntityVariablesData(String.class, "Additional_info"));
//
//        objectOuterConnections=new HashMap<>();
//        objectReferenceConnections=new HashMap<>();
//    }

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

    private Set<Phone> phones;
    private Role role;

    @Override
    public String getEntityObjectTypeForEav() {
        return null;
    }

    @Override
    public LinkedHashMap<String, EntityVariablesData> getEntityFields() {
        return null;
    }

    @Override
    public HashMap<String, EntityOuterRelationshipsData> getEntityOuterConnections() {
        return null;
    }

    @Override
    public HashMap<String, EntityReferenceRelationshipsData> getEntityReferenceConnections() {
        return null;
    }
}
