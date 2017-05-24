package projectpackage.repository.reacteav.conditions;

import projectpackage.repository.reacteav.ReacTask;

/**
 * Created by Lenovo on 24.05.2017.
 */
public interface ConditionExecutor {
    public ConditionExecutionMoment getExecutorMoment();
    public Class getExecutorClass();
    public void executeAll(ConditionExecutionMoment moment);
    public void addTask(ReacTask task);
}
