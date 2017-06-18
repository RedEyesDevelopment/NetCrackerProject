package projectpackage.repository.reacteav;

import projectpackage.repository.reacteav.conditions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 18.06.2017.
 */
public class WhereAppendingConditionExecutor implements ConditionExecutor {
    private static final ConditionExecutionMoment MOMENT = ConditionExecutionMoment.AFTER_APPENDING_WHERE;
    private List<ReactConditionData> tasks = new ArrayList<>();
    private StringBuilder builder;

    @Override
    public ConditionExecutionMoment getExecutorMoment() {
        return MOMENT;
    }

    @Override
    public Class getExecutorClass() {
        return this.getClass();
    }

    public void setBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    public boolean isThisExecutorContainsConditionForCurrentNode(ReacTask task){
        for (ReactConditionData data: tasks){
            if (data.getTargetTask().equals(task)) return true;
        }
        return false;
    }

    @Override
    public void executeAll(ConditionExecutionMoment moment) {
    }

    public void executeForTask(ReacTask task){
        for (ReactConditionData data: tasks){
            if (data.getTargetTask().equals(task)) {
                ReactConditionWhereAppending condition = (ReactConditionWhereAppending) data.getCondition();
                condition.setStringBuilder(builder);
                condition.execute();
                builder = null;
            }
        }
    }

    @Override
    public void addReactConditionData(ReactConditionData task) {
        tasks.add(task);
    }
}
