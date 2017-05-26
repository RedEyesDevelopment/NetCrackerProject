package projectpackage.repository.reacteav.conditions;

import projectpackage.repository.reacteav.ReacTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReactConditionExecutor {
    Map<Class, String> classesMap;
    private ReacTask startingTask;
    private List<String> statements;
    private ReactConditionBuilder builder;
    private List<ReactConditionThread> threads;

    public ReactConditionExecutor(Map<Class, String> classesMap, ReacTask startingTask, List<String> statements) {
        this.statements = statements;
        this.startingTask = startingTask;
        this.classesMap = classesMap;
        this.threads = new ArrayList<>();
    }

    private void createConditionThreadsList(){
        builder = new ReactConditionBuilder(classesMap, statements, startingTask);
        threads = builder.build();
    }

    public void execute(){
        createConditionThreadsList();


        }
    }
}
