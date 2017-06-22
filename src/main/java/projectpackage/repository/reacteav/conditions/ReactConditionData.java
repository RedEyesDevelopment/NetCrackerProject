package projectpackage.repository.reacteav.conditions;

import projectpackage.repository.reacteav.ReacTask;

public class ReactConditionData {
    private ReactCondition condition;
    private ReacTask targetTask;
    private ConditionExecutionMoment moment;

    public ReactConditionData(ReactCondition condition, ReacTask targetTask, ConditionExecutionMoment moment) {
        this.condition = condition;
        this.targetTask = targetTask;
        this.moment = moment;
    }

    public ReactCondition getCondition() {
        return condition;
    }

    public ReacTask getTargetTask() {
        return targetTask;
    }

    public ConditionExecutionMoment getMoment() {
        return moment;
    }
}
