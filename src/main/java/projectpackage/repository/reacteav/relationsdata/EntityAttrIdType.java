package projectpackage.repository.reacteav.relationsdata;

public class EntityAttrIdType {
    private String InnerClassObjectTypeName;
    private Integer attrId;

    public EntityAttrIdType(String innerClassObjectTypeName, Integer attrId) {
        InnerClassObjectTypeName = innerClassObjectTypeName;
        this.attrId = attrId;
    }

    public String getInnerClassObjectTypeName() {
        return InnerClassObjectTypeName;
    }

    public Integer getAttrId() {
        return attrId;
    }
}
