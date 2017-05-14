package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.annotations.ReactEntity;
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
    private static final String OBJECT_TYPE="User";
    private static final LinkedHashMap<String, EntityVariablesData> objectProperties;
    private static final HashMap<String, EntityOuterRelationshipsData> objectOuterConnections;
    private static final HashMap<String, EntityReferenceRelationshipsData> objectReferenceConnections;

    static
    {
        objectProperties = new LinkedHashMap<>();
        objectProperties.put("objectId",new EntityVariablesData(Integer.class, "%OBJECT_ID"));
        objectProperties.put("email",new EntityVariablesData(String.class, "Email"));
        objectProperties.put("password",new EntityVariablesData(String.class, "Password"));
        objectProperties.put("firstName",new EntityVariablesData(String.class, "First_name"));
        objectProperties.put("lastName",new EntityVariablesData(String.class, "Last_name"));
        objectProperties.put("additionalInfo",new EntityVariablesData(String.class, "Additional_info"));

        objectOuterConnections=new HashMap<>();
        objectReferenceConnections=new HashMap<>();
    }

    private int objectId;
    private String email;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private String additionalInfo;
    private Set<Phone> phones;

    @Override
    public String getEntityObjectTypeForEav() {
        return OBJECT_TYPE;
    }

    @Override
    public LinkedHashMap<String, EntityVariablesData> getEntityFields() {
        return objectProperties;
    }

    @Override
    public HashMap<String, EntityOuterRelationshipsData> getEntityOuterConnections() {
        return objectOuterConnections;
    }

    @Override
    public HashMap<String, EntityReferenceRelationshipsData> getEntityReferenceConnections() {
        return objectReferenceConnections;
    }
}
