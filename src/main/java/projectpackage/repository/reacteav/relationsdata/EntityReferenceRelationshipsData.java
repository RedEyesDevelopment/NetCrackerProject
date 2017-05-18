package projectpackage.repository.reacteav.relationsdata;

/**
 * Created by Lenovo on 14.05.2017.
 */
public class EntityReferenceRelationshipsData {
    private Class outerClass;
    private String outerFieldName;
    private String innerIdKey;
    private String outerIdKey;


    public EntityReferenceRelationshipsData(Class outerClass, String outerFieldName, String innerIdKey, String outerIdKey) {
        this.outerClass = outerClass;
        this.outerFieldName = outerFieldName;
        this.innerIdKey = innerIdKey;
        this.outerIdKey = outerIdKey;
    }

    public String getOuterFieldName() {
        return outerFieldName;
    }

    public String getInnerIdKey() {
        return innerIdKey;
    }

    public String getOuterIdKey() {
        return outerIdKey;
    }

    public Class getOuterClass() {
        return outerClass;
    }
}
