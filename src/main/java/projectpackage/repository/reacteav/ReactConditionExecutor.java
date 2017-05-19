package projectpackage.repository.reacteav;

import projectpackage.repository.reacteav.conditions.ReactCondition;
import projectpackage.repository.reacteav.conditions.ReactConditionBuilder;
import projectpackage.repository.reacteav.conditions.ReactConditionThread;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReactConditionExecutor {
    private ReacTask startingTask;
    private ReactCondition startingCondition;
    private String statement;
    private ReactConditionBuilder builder;
    private List<ReactConditionThread> threads;

    public ReactConditionExecutor(ReacTask startingTask, String statement) {
        this.statement = statement;
        this.startingTask = startingTask;
        this.threads = new ArrayList<>();
        createConditionList();
    }

    private void createConditionList(){
        builder = new ReactConditionBuilder(statement);
        startingCondition = builder.build();
    }

    public void execute(){
        Field startingField = null;
        try {
             startingField = startingCondition.getCurrentObjectClass().getField(startingCondition.getCurrentTargetField());
             startingField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        for (Object objects:startingTask.getResultList()){
            Object startingConditionValue = null;
            try {
                 startingConditionValue = startingField.get(objects);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            ReactConditionThread thread = new ReactConditionThread(startingConditionValue, )
        }
    }
}
