package projectpackage.repository.reacteav.relationsdata;

public class EntityReferenceRelationshipsData {
    private Class outerClass;
    private String outerFieldName;
    private String innerIdKey;
    private String outerIdKey;
    private Integer referenceAttrId;


    public EntityReferenceRelationshipsData(Class outerClass, String outerFieldName, String innerIdKey, String outerIdKey, Integer referenceAttrId) {
        this.outerClass = outerClass;
        this.outerFieldName = outerFieldName;
        this.innerIdKey = innerIdKey;
        this.outerIdKey = outerIdKey;
        this.referenceAttrId = referenceAttrId;
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

    public Integer getReferenceAttrId() {
        return referenceAttrId;
    }
}
