package projectpackage.repository.reacteav.relationsdata;

/**
 * Created by Lenovo on 14.05.2017.
 */
public class EntityReferenceRelationshipsData {
    private String outerFieldName;
    private String innerIdKey;
    private String outerIdKey;


    public EntityReferenceRelationshipsData(String outerFieldName, String innerIdKey, String outerIdKey) {
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
}
