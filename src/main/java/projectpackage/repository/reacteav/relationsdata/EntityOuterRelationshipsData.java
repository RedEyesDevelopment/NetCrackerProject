package projectpackage.repository.reacteav.relationsdata;

/**
 * Created by Lenovo on 08.05.2017.
 */
public class EntityOuterRelationshipsData {
    private String outerFieldName;
    private String innerFieldKey;
    private String outerFieldKey;

    public EntityOuterRelationshipsData(String outerFieldName, String innerFieldKey, String outerFieldKey) {
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

    public String getOuterFieldName() {
        return outerFieldName;
    }
}
