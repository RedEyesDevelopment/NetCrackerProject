package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.support.EntityVariablesNode;

import java.util.LinkedHashMap;

@Data
public class Role extends ReacEntity {
    private static final int OBJECT_TYPE=10;
    private static final LinkedHashMap<String, EntityVariablesNode> objectProperties;
    static
    {
        objectProperties = new LinkedHashMap<String, EntityVariablesNode>();
        objectProperties.put("objectId",new EntityVariablesNode<Integer>( new Integer(0), "%OBJECT_ID"));
        objectProperties.put("roleName",new EntityVariablesNode<String>( new String(), "Role_name"));
    }

    private int objectId;
    private String roleName;

    @Override
    public int getEntityObjectTypeForEav() {
        return OBJECT_TYPE;
    }

    @Override
    public LinkedHashMap<String, EntityVariablesNode> getEntityFields() {
        return objectProperties;
    }

}
