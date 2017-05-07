package projectpackage.repository.reacdao.support;

/**
 * Created by Lenovo on 07.05.2017.
 */
public class EntityVariablesNode<T> {
    private T object;
    private String databaseColumnValue;

    public EntityVariablesNode(T object, String databaseColumnValue) {
        this.object = object;
        this.databaseColumnValue = databaseColumnValue;
    }

    public T getObject() {
        return object;
    }

    public String getDatabaseColumnValue() {
        return databaseColumnValue;
    }

    public T getEntityVariable(){
        return object;
    }

}
