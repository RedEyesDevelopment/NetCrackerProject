package projectpackage.repository.reacteav.relationsdata;

/**
 * Created by Lenovo on 07.05.2017.
 */
public class EntityVariablesData {
    private Class parameterClass;
    private String databaseNativeCodeValue;
    private Integer databaseAttrtypeIdValue;

    public EntityVariablesData(Class parameterClass, String databaseNativeCodeValue, Integer databaseAttrtypeIdValue) {
        this.parameterClass = parameterClass;
        this.databaseNativeCodeValue = databaseNativeCodeValue;
        this.databaseAttrtypeIdValue = databaseAttrtypeIdValue;
    }

    public Class getParameterClass() {
        return parameterClass;
    }

    public String getDatabaseNativeCodeValue() {
        return databaseNativeCodeValue;
    }

    public Integer getDatabaseAttrtypeIdValue() {
        return databaseAttrtypeIdValue;
    }
}
