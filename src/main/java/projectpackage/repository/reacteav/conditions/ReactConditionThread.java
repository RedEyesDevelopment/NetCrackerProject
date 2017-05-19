package projectpackage.repository.reacteav.conditions;

/**
 * Created by Lenovo on 20.05.2017.
 */
public class ReactConditionThread {
    private Object startingConditionValue;
    private ReactCondition currentCondition;

    public ReactConditionThread(Object startingConditionValue, ReactCondition currentCondition) {
        this.startingConditionValue = startingConditionValue;
        this.currentCondition = currentCondition;
    }
}
