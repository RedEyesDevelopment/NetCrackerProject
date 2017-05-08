package projectpackage.repository.reacdao.support;

/**
 * Created by Lenovo on 07.05.2017.
 */
public class EntityVariablesNode {
    private Class parameterClass;
    private String databaseColumnValue;

    public EntityVariablesNode(Class parameterClass, String databaseColumnValue) {
        this.parameterClass = parameterClass;
        this.databaseColumnValue = databaseColumnValue;
    }

    public Class getParameterClass() {
        return parameterClass;
    }

    public String getDatabaseColumnValue() {
        return databaseColumnValue;
    }

}
