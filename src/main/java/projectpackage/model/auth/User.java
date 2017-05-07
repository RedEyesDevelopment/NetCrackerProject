package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.models.ReacEntityWithInnerObjects;
import projectpackage.repository.reacdao.support.EntityVariablesNode;

import java.util.LinkedHashMap;
import java.util.Set;

@Data
public class User extends ReacEntityWithInnerObjects {
    private static final int OBJECT_TYPE=3;
    private static final LinkedHashMap<String, EntityVariablesNode> objectProperties;
    private static final LinkedHashMap<String, EntityVariablesNode> objectInnerEntities;
    static
    {
        objectProperties = new LinkedHashMap<>();
        objectProperties.put("objectId",new EntityVariablesNode<Integer>( new Integer(0), "%OBJECT_ID"));
        objectProperties.put("email",new EntityVariablesNode<String>( new String(), "Email"));
        objectProperties.put("password",new EntityVariablesNode<String>( new String(), "Password"));
//        objectProperties.put("role","HAS_ROLE");
        objectProperties.put("firstName",new EntityVariablesNode<String>( new String(), "First_name"));
        objectProperties.put("lastName",new EntityVariablesNode<String>( new String(), "Last_name"));
        objectProperties.put("additionalInfo",new EntityVariablesNode<String>( new String(), "Additional_info"));

        objectInnerEntities = new LinkedHashMap<>();
//        objectInnerEntities.put("role","HAS_ROLE");
    }

    private int objectId;
    private String email;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String additionalInfo;
    private Set<Phone> phones;

    @Override
    public int getEntityObjectTypeForEav() {
        return OBJECT_TYPE;
    }

    @Override
    public LinkedHashMap<String, EntityVariablesNode> getEntityFields() {
        return objectProperties;
    }

    @Override
    public LinkedHashMap<String, EntityVariablesNode> getEntityInnerObjects() {
        return objectInnerEntities;
    }
}
