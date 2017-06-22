package projectpackage.repository.reacteav.conditions;

import projectpackage.repository.reacteav.WhereAppendingConditionExecutor;

public class StringWhereCondition implements ReactConditionWhereAppending {
    private StringBuilder builder;
    private String whereClause;

    public StringWhereCondition(String whereClause) {
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
        builder.append("\nAND ").append(whereClause);
    }
}
