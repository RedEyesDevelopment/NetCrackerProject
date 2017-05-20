package projectpackage.repository.reacteav.conditions;

import projectpackage.repository.reacteav.ReacTask;
import projectpackage.repository.reacteav.conditions.ReactCondition;
import projectpackage.repository.reacteav.conditions.ReactConditionIterator;

/**
 * Created by Lenovo on 20.05.2017.
 */
public class ReactConditionThread {
    private Object startingConditionValue;
    private ReactCondition currentCondition;
    private ReacTask startingTask;
    private ReactConditionIterator iterator;

    public ReactConditionThread(ReactCondition currentCondition, ReacTask startingTask, Object startingConditionValue) {
        this.currentCondition = currentCondition;
        this.startingConditionValue = startingConditionValue;
        this.startingTask = startingTask;
    }

    public void execute(){
        ReacTask currentTask = startingTask;
        iterator = new ReactConditionIterator(currentCondition);

            while (!currentCondition.getCurrentObjectClass().equals(currentTask.getObjectClass())){
                currentTask = currentTask.getInnerObjects()
            }
        }
    }

    private void recursionConnection(ReacTask currentTask, ReactConditionIterator iterator){
        while (iterator.hasNext()) {
            ReactCondition currentCondition = iterator.next();
            for (ReacTask task:currentTask.getInnerObjects()){
                if (currentCondition.getCurrentObjectClass().equals(task.getObjectClass()){

                }
            }
        }
    }
}
