package projectpackage.repository.reacdao.fetch;

/**
 * Created by Lenovo on 07.05.2017.
 */
public class EntityVariablesData {
    private Class parameterClass;
    private String databaseAttrtypeCodeValue;

    public EntityVariablesData(Class parameterClass, String databaseAttrtypeCodeValue) {
        this.parameterClass = parameterClass;
        this.databaseAttrtypeCodeValue = databaseAttrtypeCodeValue;
    }

    public Class getParameterClass() {
        return parameterClass;
    }

    public String getDatabaseAttrtypeCodeValue() {
        return databaseAttrtypeCodeValue;
    }
}
