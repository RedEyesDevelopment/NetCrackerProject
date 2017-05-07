package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.support.EntityVariablesNode;

import java.util.LinkedHashMap;

@Data
public class Phone extends ReacEntity {
    private static final int OBJECT_TYPE=9;
    private static final LinkedHashMap<String, EntityVariablesNode> objectProperties;
    static
    {
        objectProperties = new LinkedHashMap<String, EntityVariablesNode>();
        objectProperties.put("objectId",new EntityVariablesNode<Integer>( new Integer(0), "%OBJECT_ID"));
        objectProperties.put("phoneNumber",new EntityVariablesNode<String>( new String(), "Phone_number"));
        objectProperties.put("userId",new EntityVariablesNode<Integer>( new Integer(0), "%PARENT_ID"));
    }
    private int objectId;
    private int userId;
    private String phoneNumber;

    @Override
    public int getEntityObjectTypeForEav() {
        return OBJECT_TYPE;
    }

    @Override
    public LinkedHashMap<String, EntityVariablesNode> getEntityFields() {
        return objectProperties;
    }
}
