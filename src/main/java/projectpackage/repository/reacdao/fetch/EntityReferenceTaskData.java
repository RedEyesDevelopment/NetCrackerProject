package projectpackage.repository.reacdao.fetch;

/**
 * Created by Lenovo on 14.05.2017.
 */
public class EntityReferenceTaskData {
    private Class thisClass;
    private Class innerClass;
    private String innerClassObjectTypeName;
    private String thisFieldName;
    private String innerIdKey;
    private String thisIdKey;
    private String innerIdParameterNameForQueryParametersMap;
    private int innerObjectIdForInsertion;

    public EntityReferenceTaskData(Class thisClass, Class innerClass, String innerClassObjectTypeName, String thisFieldName, String innerIdKey, String thisIdKey) {
        this.thisClass = thisClass;
        this.innerClass = innerClass;
        this.innerClassObjectTypeName = innerClassObjectTypeName;
        this.thisFieldName = thisFieldName;
        this.innerIdKey = innerIdKey;
        this.thisIdKey = thisIdKey;
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

    public int getInnerObjectIdForInsertion() {
        return innerObjectIdForInsertion;
    }

    public void setInnerObjectIdForInsertion(int innerObjectIdForInsertion) {
        this.innerObjectIdForInsertion = innerObjectIdForInsertion;
    }

    public String getInnerIdParameterNameForQueryParametersMap() {
        return innerIdParameterNameForQueryParametersMap;
    }

    public void setInnerIdParameterNameForQueryParametersMap(String innerIdParameterNameForQueryParametersMap) {
        this.innerIdParameterNameForQueryParametersMap = innerIdParameterNameForQueryParametersMap;
    }
}
