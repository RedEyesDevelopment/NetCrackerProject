package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.fetch.EntityOuterRelationshipsData;
import projectpackage.repository.reacdao.fetch.EntityReferenceRelationshipsData;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.fetch.EntityVariablesData;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Data
public class Phone extends ReacEntity {
    private static final String OBJECT_TYPE="Phone";
    private static final LinkedHashMap<String, EntityVariablesData> objectProperties;
    private static final HashMap<String, EntityOuterRelationshipsData> objectOuterConnections;
    private static final HashMap<String, EntityReferenceRelationshipsData> objectReferenceConnections;


    static
    {
        objectProperties = new LinkedHashMap<String, EntityVariablesData>();
        objectProperties.put("objectId",new EntityVariablesData(Integer.class, "%OBJECT_ID"));
        objectProperties.put("phoneNumber",new EntityVariablesData(String.class, "Phone_number"));
        objectProperties.put("userId",new EntityVariablesData(Integer.class, "%PARENT_ID"));

        objectOuterConnections=new HashMap<>();
        objectOuterConnections.put("phonesForUser", new EntityOuterRelationshipsData(User.class, "phones","userId", "objectId"));

        objectReferenceConnections=new HashMap<>();
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
    public HashMap<String, EntityOuterRelationshipsData> getEntityOuterConnections() {
        return objectOuterConnections;
    }

    @Override
    public HashMap<String, EntityReferenceRelationshipsData> getEntityReferenceConnections() {
        return objectReferenceConnections;
    }

}
