package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.fetch.EntityOuterRelationshipsData;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.fetch.EntityVariablesData;

import java.util.LinkedHashMap;

@Data
public class Phone extends ReacEntity {
    private static final String OBJECT_TYPE="Phone";
    private static final LinkedHashMap<String, EntityVariablesData> objectProperties;
    static
    {
        objectProperties = new LinkedHashMap<String, EntityVariablesData>();
        objectProperties.put("objectId",new EntityVariablesData(Integer.class, "%OBJECT_ID"));
        objectProperties.put("phoneNumber",new EntityVariablesData(String.class, "Phone_number"));
        objectProperties.put("userId",new EntityVariablesData(Integer.class, "%PARENT_ID"));
    }
    private int objectId;
    private int userId;
    private String phoneNumber;

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
