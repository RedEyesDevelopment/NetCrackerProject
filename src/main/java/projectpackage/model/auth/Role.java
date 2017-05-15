package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.annotations.ReactEntity;
import projectpackage.repository.reacdao.annotations.ReactField;
import projectpackage.repository.reacdao.annotations.ReactReference;
import projectpackage.repository.reacdao.fetch.EntityOuterRelationshipsData;
import projectpackage.repository.reacdao.fetch.EntityReferenceRelationshipsData;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.fetch.EntityVariablesData;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Data
@ReactEntity(entityTypeName = "Role")
@ReactReference(outerEntityClass = User.class, outerFieldName = "role", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Role extends ReacEntity {
//    private static final String OBJECT_TYPE="Role";
//    private static final LinkedHashMap<String, EntityVariablesData> objectProperties;
//    private static final HashMap<String, EntityOuterRelationshipsData> objectOuterConnections;
//    private static final HashMap<String, EntityReferenceRelationshipsData> objectReferenceConnections;
//
//
//    static
//    {
//        objectProperties = new LinkedHashMap<String, EntityVariablesData>();
//        objectProperties.put("objectId",new EntityVariablesData( Integer.class, "%OBJECT_ID"));
//        objectProperties.put("roleName",new EntityVariablesData( String.class, "Role_name"));
//
//        objectOuterConnections=new HashMap<>();
//
//        objectReferenceConnections=new HashMap<>();
//        objectReferenceConnections.put("roleForUser",  new EntityReferenceRelationshipsData(User.class, OBJECT_TYPE, "role","objectId", "objectId"));
//    }
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Role_name")
    private String roleName;

    @Override
    public String getEntityObjectTypeForEav() {
        return null;
    }

    @Override
    public LinkedHashMap<String, EntityVariablesData> getEntityFields() {
        return null;
    }

    @Override
    public HashMap<String, EntityOuterRelationshipsData> getEntityOuterConnections() {
        return null;
    }

    @Override
    public HashMap<String, EntityReferenceRelationshipsData> getEntityReferenceConnections() {
        return null;
    }

}
