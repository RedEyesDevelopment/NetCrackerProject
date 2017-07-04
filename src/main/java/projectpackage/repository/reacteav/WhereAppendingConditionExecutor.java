package projectpackage.repository.reacteav;

import projectpackage.repository.reacteav.conditions.*;

import java.util.ArrayList;
import java.util.List;

public class WhereAppendingConditionExecutor implements ConditionExecutor {
    private static final ConditionExecutionMoment MOMENT = ConditionExecutionMoment.AFTER_APPENDING_WHERE;
    private List<ReactConditionData> tasks = new ArrayList<>();
    private StringBuilder builder;

    @Override
    public ConditionExecutionMoment getExecutorMoment() {
        return MOMENT;
    }

    @Override
    public Class getExecutorClass() {
        return this.getClass();
    }

    void setBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    boolean isThisExecutorContainsConditionForCurrentNode(ReacTask task){
        for (ReactConditionData data: tasks){
            if (data.getTargetTask().equals(task)) {
                return true;
            }
        }
        return false;
    }

    boolean isThisExecutorContainsVariableConditionForCurrentNode(ReacTask task){
        for (ReactConditionData data: tasks){
            if (data.getCondition().getClass().equals(VariableWhereCondition.class)){
                if (data.getTargetTask().equals(task)) {
                    return true;
                }
            }
        }
        return false;
    }

    void checkAndInsertVariableToConditionIfEquals(String variableName, String columnName, ReacTask task){
        for (ReactConditionData data: tasks){
            if (data.getCondition().getClass().equals(VariableWhereCondition.class)){
                if (data.getTargetTask().equals(task)) {
                    VariableWhereCondition variableWhereCondition = (VariableWhereCondition) data.getCondition();
                    if (variableWhereCondition.columnNameCheck(variableName)){
                        variableWhereCondition.setDatabaseQueryColumnName(columnName);
                    }
                }
            }
        }
    }

    @Override
    public void executeAll(ConditionExecutionMoment moment) {
    }

    void executeForTask(ReacTask task){
        for (ReactConditionData data: tasks){
            if (data.getTargetTask().equals(task)) {
                ReactConditionWhereAppending condition = (ReactConditionWhereAppending) data.getCondition();
                condition.setStringBuilder(builder);
                condition.execute();
                builder = null;
            }
        }
    }

    @Override
    public void addReactConditionData(ReactConditionData task) {
        tasks.add(task);
    }
}
