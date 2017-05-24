package projectpackage.repository.reacteav.conditions;

/**
 * Created by Lenovo on 24.05.2017.
 */
public interface ConditionExecutor {
    public ConditionExecutionMoment getExecutorMoment();
    public Class getExecutorClass();
    public void executeAll(ConditionExecutionMoment moment);
    public void addReactConditionData(ReactConditionData task);
}
