package projectpackage.repository.reacteav.conditions;

import projectpackage.repository.reacteav.WhereAppendingConditionExecutor;

/**
 * Created by Lenovo on 18.06.2017.
 */
public class AppendingStringWhereConditionAfterExecution implements ReactConditionWhereAppending {
    private StringBuilder builder;
    private String whereClause;

    public AppendingStringWhereConditionAfterExecution(String whereClause) {
        this.whereClause = whereClause;
    }

    @Override
    public Class getNeededConditionExecutor() {
        return WhereAppendingConditionExecutor.class;
    }

    @Override
    public void setStringBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void execute() {
        builder.append(whereClause);
    }
}
