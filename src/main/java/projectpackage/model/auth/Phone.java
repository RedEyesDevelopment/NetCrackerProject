package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.fetch.EntityInnerObjectNode;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.fetch.EntityVariablesNode;

import java.util.LinkedHashMap;

@Data
public class Phone extends ReacEntity {
    private static final String OBJECT_TYPE="Phone";
    private static final LinkedHashMap<String, EntityVariablesNode> objectProperties;
    static
    {
        objectProperties = new LinkedHashMap<String, EntityVariablesNode>();
        objectProperties.put("objectId",new EntityVariablesNode(Integer.class, "%OBJECT_ID"));
        objectProperties.put("phoneNumber",new EntityVariablesNode(String.class, "Phone_number"));
        objectProperties.put("userId",new EntityVariablesNode(Integer.class, "%PARENT_ID"));
    }
    private int objectId;
    private int userId;
    private String phoneNumber;

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
        return null;
    }
}
