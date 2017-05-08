package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.fetch.EntityInnerObjectNode;
import projectpackage.repository.reacdao.fetch.EntityVariablesNode;
import projectpackage.repository.reacdao.models.ReacEntity;

import java.util.LinkedHashMap;
import java.util.Set;

@Data
public class User extends ReacEntity {
    private static final String OBJECT_TYPE="User";
    private static final LinkedHashMap<String, EntityVariablesNode> objectProperties;
    private static final LinkedHashMap<String, EntityInnerObjectNode> objectInnerEntities;
    static
    {
        objectProperties = new LinkedHashMap<>();
        objectProperties.put("objectId",new EntityVariablesNode(Integer.class, "%OBJECT_ID"));
        objectProperties.put("email",new EntityVariablesNode(String.class, "Email"));
        objectProperties.put("password",new EntityVariablesNode(String.class, "Password"));
        objectProperties.put("firstName",new EntityVariablesNode(String.class, "First_name"));
        objectProperties.put("lastName",new EntityVariablesNode(String.class, "Last_name"));
        objectProperties.put("additionalInfo",new EntityVariablesNode(String.class, "Additional_info"));

        objectInnerEntities = new LinkedHashMap<>();
        objectInnerEntities.put("role",new EntityInnerObjectNode(Role.class, "HAS_ROLE"));
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
    public String getEntityObjectTypeForEav() {
        return OBJECT_TYPE;
    }

    @Override
    public LinkedHashMap<String, EntityVariablesNode> getEntityFields() {
        return objectProperties;
    }

    @Override
    public LinkedHashMap<String, EntityInnerObjectNode> getEntityInnerObjects() {
        return objectInnerEntities;
    }
}
