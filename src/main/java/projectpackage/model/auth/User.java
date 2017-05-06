package projectpackage.model.auth;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import projectpackage.model.ReacEntityWithInnerObjects;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Data
public class User extends ReacEntityWithInnerObjects {
    private static final int OBJECT_TYPE=3;
    private static final HashMap<String, String> objectProperties;
    private static final HashMap<String, String> objectInnerEntities;
    static
    {
        objectProperties = new LinkedHashMap<>();
        objectProperties.put("objectId","%OBJECT_ID");
        objectProperties.put("email","EMAIL");
        objectProperties.put("password","PASSWORD");
//        objectProperties.put("role","HAS_ROLE");
        objectProperties.put("firstName","FIRST_NAME");
        objectProperties.put("lastName","LAST_NAME");
        objectProperties.put("additionalInfo","ADDITIONAL_INFO");

        objectInnerEntities = new HashMap<>();
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
        return ImmutableMap.copyOf(objectProperties);
    }

    @Override
    public Map<String, String> getEntityInnerObjects() {
        return ImmutableMap.copyOf(objectInnerEntities);
    }
}
