package projectpackage.model.auth;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import projectpackage.repository.reacdao.models.ReacEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Role extends ReacEntity {
    private static final int OBJECT_TYPE=10;
    private static final HashMap<String, String> objectProperties;
    static
    {
        objectProperties = new LinkedHashMap<>();
        objectProperties.put("objectId","%OBJECT_ID");
        objectProperties.put("roleName","ROLE_NAME");
        objectProperties.put("userId","%PARENT_ID");

    }

    private int objectId;
    private String roleName;

    @Override
    public int getEntityObjectTypeForEav() {
        return OBJECT_TYPE;
    }

    @Override
    public Map<String, String> getEntityFields() {
        return ImmutableMap.copyOf(objectProperties);
    }

}
