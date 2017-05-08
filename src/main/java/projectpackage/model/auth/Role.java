package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.fetch.EntityInnerObjectNode;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.fetch.EntityVariablesNode;

import java.util.LinkedHashMap;

@Data
public class Role extends ReacEntity {
    private static final String OBJECT_TYPE="Role";
    private static final LinkedHashMap<String, EntityVariablesNode> objectProperties;
    static
    {
        objectProperties = new LinkedHashMap<String, EntityVariablesNode>();
        objectProperties.put("objectId",new EntityVariablesNode( Integer.class, "%OBJECT_ID"));
        objectProperties.put("roleName",new EntityVariablesNode( String.class, "Role_name"));
    }

    private int objectId;
    private String roleName;

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
