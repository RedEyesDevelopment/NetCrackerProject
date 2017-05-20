package projectpackage.repository.reacteav.conditions;

import java.util.Iterator;

/**
 * Created by Lenovo on 20.05.2017.
 */
public class ReactConditionIterator implements Iterator<ReactCondition> {
    private ReactCondition firstCondition;
    private ReactCondition currentCondition;

    public ReactConditionIterator(ReactCondition condition) {
        this.currentCondition = condition;
        this.firstCondition = condition;
    }

    @Override
    public boolean hasNext() {
        return (null!=currentCondition.getNextCondition());
    }

    @Override
    public ReactCondition next() {
        currentCondition = currentCondition.getNextCondition();
        return currentCondition;
    }

    public ReactCondition getFirst(){
        return firstCondition;
    }
}
