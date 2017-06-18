package projectpackage.repository.reacteav.conditions;

/**
 * Created by Lenovo on 18.06.2017.
 */
public interface ReactCondition {
    public Class getNeededConditionExecutor();
    public void execute();
}
