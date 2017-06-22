package projectpackage.repository.reacteav.conditions;

public interface ConditionExecutor {
    public ConditionExecutionMoment getExecutorMoment();
    public Class getExecutorClass();
    public void executeAll(ConditionExecutionMoment moment);
    public void addReactConditionData(ReactConditionData task);
}
