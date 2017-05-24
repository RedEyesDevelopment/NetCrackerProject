package projectpackage.repository.reacteav.conditions;

import projectpackage.repository.reacteav.ReacTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 24.05.2017.
 */
public class TaskToTaskConditionExecutor implements ConditionExecutor {
    private static final ConditionExecutionMoment MOMENT= ConditionExecutionMoment.AFTER_QUERY;
    private List<ReacTask> tasks = new ArrayList<>();

    @Override
    public ConditionExecutionMoment getExecutorMoment() {
        return MOMENT;
    }

    @Override
    public Class getExecutorClass() {
        return this.getClass();
    }

        @Override
        public void executeAll(ConditionExecutionMoment moment) {
            if (moment.equals(MOMENT)){
                for (ReacTask task:tasks){
                    List<ReactConditionData> taskData = task.get
                }

            }
    }

    @Override
    public void addTask(ReacTask task) {
        tasks.add(task);
    }
}
