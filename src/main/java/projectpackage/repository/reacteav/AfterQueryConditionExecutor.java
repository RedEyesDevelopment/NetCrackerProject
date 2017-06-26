package projectpackage.repository.reacteav;

import projectpackage.repository.reacteav.conditions.*;

import java.util.ArrayList;
import java.util.List;

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
                        ReactConditionAfterExecution condition = (ReactConditionAfterExecution) task.getCondition();
                        condition.loadDataToParse(task.getTargetTask().getResultList());
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
