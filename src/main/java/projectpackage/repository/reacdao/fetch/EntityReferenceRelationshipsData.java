package projectpackage.repository.reacdao.fetch;

import projectpackage.repository.reacdao.models.ReacEntity;

/**
 * Created by Lenovo on 14.05.2017.
 */
public class EntityReferenceRelationshipsData {
    private Class<? extends ReacEntity> outerClass;
    private Class<? extends ReacEntity> thisClass;
    private String thisClassObjectTypeName;
    private String outerFieldName;
    private String innerIdKey;
    private String outerIdKey;


    public EntityReferenceRelationshipsData(Class<? extends ReacEntity> outerClass, String thisClassObjectTypeName, String outerFieldName, String innerIdKey, String outerIdKey) {
        this.outerClass = outerClass;
        this.thisClassObjectTypeName = thisClassObjectTypeName;
        this.outerFieldName = outerFieldName;
        this.innerIdKey = innerIdKey;
        this.outerIdKey = outerIdKey;
    }

    public Class<? extends ReacEntity> getOuterClass() {
        return outerClass;
    }

    public String getThisClassObjectTypeName() {
        return thisClassObjectTypeName;
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

    public Class<? extends ReacEntity> getThisClass() {
        return thisClass;
    }

    public void setThisClass(Class<? extends ReacEntity> thisClass) {
        this.thisClass = thisClass;
    }
}
