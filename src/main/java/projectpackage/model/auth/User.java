package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.models.ReacEntityWithInnerObjects;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Data
public class User extends ReacEntityWithInnerObjects {
    private static final int OBJECT_TYPE=3;
    private static final LinkedHashMap<String, String> objectProperties;
    private static final LinkedHashMap<String, String> objectInnerEntities;
    static
    {
        objectProperties = new LinkedHashMap<>();
        objectProperties.put("objectId","%OBJECT_ID");
        objectProperties.put("email","Email");
        objectProperties.put("password","Password");
//        objectProperties.put("role","HAS_ROLE");
        objectProperties.put("firstName","First_name");
        objectProperties.put("lastName","Last_name");
        objectProperties.put("additionalInfo","Additional_info");

        objectInnerEntities = new LinkedHashMap<>();
        objectInnerEntities.put("role","HAS_ROLE");
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
    public Map<String, String> getEntityFields() {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>(objectProperties);
        return fields;
    }

    @Override
    public Map<String, String> getEntityInnerObjects() {
        LinkedHashMap<String, String> inner = new LinkedHashMap<>(objectInnerEntities);
        return inner;
    }
}
