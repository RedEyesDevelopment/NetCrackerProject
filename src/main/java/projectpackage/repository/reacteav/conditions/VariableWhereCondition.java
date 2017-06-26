package projectpackage.repository.reacteav.conditions;

import projectpackage.repository.reacteav.WhereAppendingConditionExecutor;

public class VariableWhereCondition implements ReactConditionWhereAppending {
    private StringBuilder builder;
    private String condition;
    private String columnName;
    private String databaseQueryColumnName;

    public VariableWhereCondition(String columnName, String condition) {
        this.condition = condition;
        this.columnName = columnName;
    }

    public boolean columnNameCheck(String columnName){
        return this.columnName.equals(columnName);
    }

    public void setDatabaseQueryColumnName(String databaseQueryColumnName) {
        this.databaseQueryColumnName = databaseQueryColumnName;
    }

    @Override
    public Class getNeededConditionExecutor() {
        return WhereAppendingConditionExecutor.class;
    }

    @Override
    public void execute() {
        if (null!=databaseQueryColumnName){
            StringBuilder query = new StringBuilder("\n AND ");
            query.append(databaseQueryColumnName);
            query.append(" = '");
            query.append(condition);
            query.append("'");
            builder.append(query.toString());
        }
    }

    @Override
    public void setStringBuilder(StringBuilder builder) {
        this.builder = builder;
    }
}
