package projectpackage.repository.reacdao.fetch;

import projectpackage.repository.reacdao.models.ReacEntity;

/**
 * Created by Lenovo on 08.05.2017.
 */
public class EntityOuterRelationshipsData {
    private Class<? extends ReacEntity> outerClass;
    private String outerFieldName;
    private String innerFieldKey;
    private String outerFieldKey;

    public EntityOuterRelationshipsData(Class<? extends ReacEntity> outerClass, String outerFieldName, String innerFieldKey, String outerFieldKey) {
        this.outerClass = outerClass;
        this.outerFieldName = outerFieldName;
        this.innerFieldKey = innerFieldKey;
        this.outerFieldKey = outerFieldKey;
    }

    public String getInnerFieldKey() {
        return innerFieldKey;
    }

    public String getOuterFieldKey() {
        return outerFieldKey;
    }

    public Class<? extends ReacEntity> getOuterClass() {
        return outerClass;
    }

    public String getOuterFieldName() {
        return outerFieldName;
    }
}
