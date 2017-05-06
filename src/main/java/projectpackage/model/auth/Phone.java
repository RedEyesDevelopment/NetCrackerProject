package projectpackage.model.auth;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import projectpackage.model.ReacEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Phone extends ReacEntity {
    private static final int OBJECT_TYPE=9;
    private static final HashMap<String, String> objectProperties;
    static
    {
        objectProperties = new LinkedHashMap<>();
        objectProperties.put("objectId","%OBJECT_ID");
        objectProperties.put("phoneNumber","PHONE_NUMBER");
        objectProperties.put("userId","%PARENT_ID");
    }
    private int objectId;
    private int userId;
    private String phoneNumber;

    @Override
    public int getEntityObjectTypeForEav() {
        return OBJECT_TYPE;
    }

    @Override
    public Map<String, String> getEntityFields() {
        return ImmutableMap.copyOf(objectProperties);
    }
}
