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

    @Override
    public void executeAll(ConditionExecutionMoment moment) {
        if (moment.equals(MOMENT)) {
            for (ReactConditionData task : tasks) {
                if (moment.equals(task.getMoment())) {
                    ReactConditionWhereAppending condition = (ReactConditionWhereAppending) task.getCondition();
                    condition.setStringBuilder(builder);
                    condition.execute();
                }
            }
        }
    }

    @Override
    public void addReactConditionData(ReactConditionData task) {
        tasks.add(task);
    }
}
