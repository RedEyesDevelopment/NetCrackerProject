package projectpackage.repository.reacteav.relationsdata;

public class EntityReferenceTaskData {
    private Class thisClass;
    private Class innerClass;
    private String innerClassObjectTypeName;
    private String thisFieldName;
    private String innerIdKey;
    private String thisIdKey;
    private Integer referenceAttrId;
    private String innerIdParameterNameForQueryParametersMap;

    public EntityReferenceTaskData(Class thisClass, Class innerClass, String innerClassObjectTypeName, String thisFieldName, String innerIdKey, String thisIdKey, Integer referenceAttrId) {
        this.thisClass = thisClass;
        this.innerClass = innerClass;
        this.innerClassObjectTypeName = innerClassObjectTypeName;
        this.thisFieldName = thisFieldName;
        this.innerIdKey = innerIdKey;
        this.thisIdKey = thisIdKey;
        this.referenceAttrId = referenceAttrId;
    }

    public Class getThisClass() {
        return thisClass;
    }

    public Class getInnerClass() {
        return innerClass;
    }

    public String getInnerClassObjectTypeName() {
        return innerClassObjectTypeName;
    }

    public String getThisFieldName() {
        return thisFieldName;
    }

    public String getInnerIdKey() {
        return innerIdKey;
    }

    public String getThisIdKey() {
        return thisIdKey;
    }

    public Integer getReferenceAttrId() {
        return referenceAttrId;
    }

    public String getInnerIdParameterNameForQueryParametersMap() {
        return innerIdParameterNameForQueryParametersMap;
    }

    public void setInnerIdParameterNameForQueryParametersMap(String innerIdParameterNameForQueryParametersMap) {
        this.innerIdParameterNameForQueryParametersMap = innerIdParameterNameForQueryParametersMap;
    }
}
