package projectpackage.repository.reacteav.conditions;

import projectpackage.repository.reacteav.ReacTask;

/**
 * Created by Lenovo on 20.05.2017.
 */
public class ReactConditionData {
    private ReactCondition condition;
    private ReacTask startingTask;
    private ConditionExecutionMoment moment;

    public ReactConditionData(ReactCondition condition, ReacTask startingTask, ConditionExecutionMoment moment) {
        this.condition = condition;
        this.startingTask = startingTask;
        this.moment = moment;
    }

    public ReactCondition getCondition() {
        return condition;
    }

    public ReacTask getStartingTask() {
        return startingTask;
    }

    public ConditionExecutionMoment getMoment() {
        return moment;
    }
}
