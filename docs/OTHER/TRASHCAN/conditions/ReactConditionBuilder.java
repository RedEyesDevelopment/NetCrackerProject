package projectpackage.repository.reacteav.conditions;

import projectpackage.repository.reacteav.ReacTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReactConditionBuilder {
    private ReacTask startingTask;
    private List<String> statements;
    private Map<Class, String> classesMap;
    private List<ReactCondition> rootConditions;
    private List<ReactConditionThread> threads;

    public ReactConditionBuilder(Map<Class, String> classesMap, List<String> statements, ReacTask startingTask) {
        this.statements = statements;
        this.startingTask = startingTask;
        this.classesMap = classesMap;
    }

    public List<ReactConditionThread> build() {
        for (String statement : statements) {
            List<ReactStringCondition> stringConditions = new ArrayList<>();
            String[] lines = statement.split("-");
            for (String currentLine : lines) {
                String[] tokens = currentLine.split("[():]");
                ReactStringCondition newCondition = new ReactStringCondition(tokens[0], tokens[1], tokens[2]);
                stringConditions.add(newCondition);
            }
            ReactCondition previousCondition = null;
            ReactCondition rootCondition = null;
            for (ReactStringCondition stringCondition : stringConditions) {
                ReactCondition condition;
                Class clazz = null;
                for (Class currClass : classesMap.keySet()) {
                    if (currClass.getSimpleName().equals(stringCondition.getCurrentClass())) {
                        clazz = currClass;
                    }
                }
                condition = new ReactCondition(clazz, stringCondition.getTargetFieldName(), stringCondition.getNextClassFieldName(), previousCondition);
                if (null != previousCondition) {
                    previousCondition.setNextCondition(condition);
                } else {
                    rootCondition = condition;
                }
                previousCondition = condition;
            }
            rootConditions.add(rootCondition);
        }
        for (ReactCondition condition : rootConditions) {
            Object startingObjectValue = null;
            while (null == startingObjectValue) {
                if (condition.getCurrentObjectClass().equals(startingTask.getObjectClass())) {
                    try {
                        Field field = condition.getCurrentObjectClass().getDeclaredField(condition.getCurrentTargetField());
                        field.setAccessible(true);
                        for (Object objects:startingTask.getResultList()){
                            try {
                                startingObjectValue = field.get(startingTask.getResultList());
                            } catch (IllegalAccessException e) {
                                LOGGER.error(e);
                            }
                            ReactConditionThread thread = new ReactConditionThread(condition, startingTask, startingObjectValue);
                            threads.add(thread);
                        }

                    } catch (NoSuchFieldException e) {
                        LOGGER.error(e);
                    }
                }
            }
        }
        return threads;
    }
}

