package projectpackage.repository.reacteav.relationsdata;

public class EntityReferenceTaskData {
    private Class innerClass;
    private String innerClassObjectTypeName;
    private String thisFieldName;
    private Integer referenceAttrId;
    private String innerIdParameterNameForQueryParametersMap;

    public EntityReferenceTaskData(Class innerClass, String innerClassObjectTypeName, String thisFieldName, Integer referenceAttrId) {
        this.innerClass = innerClass;
        this.innerClassObjectTypeName = innerClassObjectTypeName;
        this.thisFieldName = thisFieldName;
        this.referenceAttrId = referenceAttrId;
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
