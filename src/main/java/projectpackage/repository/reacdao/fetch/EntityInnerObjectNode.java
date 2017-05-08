package projectpackage.repository.reacdao.fetch;

/**
 * Created by Lenovo on 08.05.2017.
 */
public class EntityInnerObjectNode {
    private Class parameterClass;
    private String databaseAttrtypeCodeValue;

    public EntityInnerObjectNode(Class parameterClass, String databaseAttrtypeCodeValue) {
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
