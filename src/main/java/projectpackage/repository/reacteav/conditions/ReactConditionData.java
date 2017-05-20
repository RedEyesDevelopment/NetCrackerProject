package projectpackage.repository.reacteav.conditions;

/**
 * Created by Lenovo on 20.05.2017.
 */
public class ReactConditionData {
    private ReactCondition condition;
    private ConditionExecutionMoment moment;

    public ReactConditionData(ReactCondition condition, ConditionExecutionMoment moment) {
        this.condition = condition;
        this.moment = moment;
    }

    public ReactCondition getCondition() {
        return condition;
    }

    public ConditionExecutionMoment getMoment() {
        return moment;
    }
}
