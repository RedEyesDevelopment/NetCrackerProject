package projectpackage.model.auth;

import lombok.Data;
import projectpackage.repository.reacdao.annotations.ReactChild;
import projectpackage.repository.reacdao.annotations.ReactEntity;
import projectpackage.repository.reacdao.annotations.ReactField;
import projectpackage.repository.reacdao.fetch.EntityOuterRelationshipsData;
import projectpackage.repository.reacdao.fetch.EntityReferenceRelationshipsData;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.fetch.EntityVariablesData;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Data
@ReactEntity(entityTypeName = "Phone")
@ReactChild(outerEntityClass = User.class, outerFieldName = "phones", outerFieldKey = "objectId", innerFieldKey = "userId")
public class Phone extends ReacEntity {
    //    private static final String OBJECT_TYPE="Phone";
//    private static final LinkedHashMap<String, EntityVariablesData> objectProperties;
//    private static final HashMap<String, EntityOuterRelationshipsData> objectOuterConnections;
//    private static final HashMap<String, EntityReferenceRelationshipsData> objectReferenceConnections;
//
//
//    static
//    {
//        objectProperties = new LinkedHashMap<String, EntityVariablesData>();
//        objectProperties.put("objectId",new EntityVariablesData(Integer.class, "%OBJECT_ID"));
//        objectProperties.put("phoneNumber",new EntityVariablesData(String.class, "Phone_number"));
//        objectProperties.put("userId",new EntityVariablesData(Integer.class, "%PARENT_ID"));
//
//        objectOuterConnections=new HashMap<>();
//        objectOuterConnections.put("phonesForUser", new EntityOuterRelationshipsData(User.class, "phones","userId", "objectId"));
//
//        objectReferenceConnections=new HashMap<>();
//    }    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%PARENT_ID")
    private int userId;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Phone_number")
    private String phoneNumber;

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
