package projectpackage.repository.reacteav;

import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.ConditionExecutor;
import projectpackage.repository.reacteav.conditions.ReactCondition;
import projectpackage.repository.reacteav.conditions.ReactConditionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 24.05.2017.
 */
public class AfterQueryConditionExecutor implements ConditionExecutor {
    private static final ConditionExecutionMoment MOMENT= ConditionExecutionMoment.AFTER_QUERY;
    private List<ReactConditionData> tasks = new ArrayList<>();

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
                for (ReactConditionData task:tasks){
                    if (moment.equals(task.getMoment())){
                        ReactCondition condition = task.getCondition();
                        condition.loadDataToParse(task.getStartingTask().getResultList());
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
