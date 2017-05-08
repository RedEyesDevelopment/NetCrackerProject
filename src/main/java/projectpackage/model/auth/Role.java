package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.fetch.EntityOuterRelationshipsData;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.fetch.EntityVariablesData;

import java.util.LinkedHashMap;

@Data
public class Role extends ReacEntity {
    private static final String OBJECT_TYPE="Role";
    private static final LinkedHashMap<String, EntityVariablesData> objectProperties;
    static
    {
        objectProperties = new LinkedHashMap<String, EntityVariablesData>();
        objectProperties.put("objectId",new EntityVariablesData( Integer.class, "%OBJECT_ID"));
        objectProperties.put("roleName",new EntityVariablesData( String.class, "Role_name"));
    }

    private int objectId;
    private String roleName;

    @Override
    public String getEntityObjectTypeForEav() {
        return OBJECT_TYPE;
    }

    @Override
    public LinkedHashMap<String, EntityVariablesData> getEntityFields() {
        return objectProperties;
    }

    @Override
    public LinkedHashMap<String, EntityOuterRelationshipsData> getEntityInnerObjects() {
        return null;
    }

}
