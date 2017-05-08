package projectpackage.repository.reacdao.fetch;

/**
 * Created by Lenovo on 08.05.2017.
 */
public class EntityOuterRelationshipsData {
    private String outerFieldName;
    private String innerFieldKey;
    private String outerFieldKey;
    private String databaseAttrtypeCodeValue;

    public EntityOuterRelationshipsData(String outerFieldName, String innerFieldKey, String outerFieldKey, String databaseAttrtypeCodeValue) {
        this.outerFieldName = outerFieldName;
        this.innerFieldKey = innerFieldKey;
        this.outerFieldKey = outerFieldKey;
        this.databaseAttrtypeCodeValue = databaseAttrtypeCodeValue;
    }

    public String getOuterFieldName() {
        return outerFieldName;
    }

    public String getInnerFieldKey() {
        return innerFieldKey;
    }

    public String getOuterFieldKey() {
        return outerFieldKey;
    }

    public String getDatabaseAttrtypeCodeValue() {
        return databaseAttrtypeCodeValue;
    }
}
