package projectpackage.repository.reacteav.querying;

import projectpackage.repository.reacteav.support.ReactConstantConfiguration;

public class ReactQueryAppender {
    private StringBuilder queryBuilder;
    private ReactConstantConfiguration config;
    private boolean firstWordInSelectQueryFlag = true;
    private boolean firstWordInFromQueryFlag = true;
    private boolean firstWordInWhereQueryFlag = true;

    public ReactQueryAppender(StringBuilder queryBuilder, ReactConstantConfiguration config) {
        this.queryBuilder = queryBuilder;
        this.config = config;
    }

    public void appendSelectColumnWithNaming(String tableName, String column, String nameOfColumn) {
        if (!firstWordInSelectQueryFlag) {
            appendKoma();
        }
        queryBuilder.append(tableName).append(config.getPointChar()).append(column).append(config.getSpaceChar()).append(config.getDoubleBracket()).append(nameOfColumn).append(config.getDoubleBracket());
        firstWordInSelectQueryFlag = false;
    }

    public void appendSelectColumnWithValueAndNaming(String tableName, String nameOfColumn) {
        if (!firstWordInSelectQueryFlag) {
            appendKoma();
        }
        queryBuilder.append(tableName).append(config.getPointChar()).append(config.getV()).append(config.getSpaceChar()).append(config.getDoubleBracket()).append(nameOfColumn).append(config.getDoubleBracket());
        firstWordInSelectQueryFlag = false;
    }

    public void appendSelectColumnWithDataValueAndNaming(String tableName, String nameOfColumn) {
        if (!firstWordInSelectQueryFlag) {
            appendKoma();
        }
        queryBuilder.append(tableName).append(config.getPointChar()).append(config.getDv()).append(config.getSpaceChar()).append(config.getDoubleBracket()).append(nameOfColumn).append(config.getDateAppender()).append(config.getDoubleBracket());
        firstWordInSelectQueryFlag = false;
    }

    public void appendSelectWord() {
        queryBuilder.append(config.getSelectWord()).append(config.getSpaceChar());
    }

    public void appendFromWord() {
        queryBuilder.append(config.getNewLineChar()).append(config.getFromWord()).append(config.getSpaceChar());
    }

    public void appendWhereWord() {
        queryBuilder.append(config.getNewLineChar()).append(config.getWhereWord()).append(config.getSpaceChar());

    }

    public void appendFromTableWithNaming(String tableName, String tableAlias) {
        if (!firstWordInFromQueryFlag) {
            appendKoma();
        }
        queryBuilder.append(tableName).append(config.getSpaceChar()).append(tableAlias);
        firstWordInFromQueryFlag = false;
    }

    private void appendKoma() {
        queryBuilder.append(", ");
    }

    private void appendAnd() {
        queryBuilder.append(config.getNewLineAnd());
    }

    public void appendWhereConditionWithTableIdEqualsToConstantInt(String tableName, String constant) {
        if (!firstWordInWhereQueryFlag) {
            appendAnd();
        }
        queryBuilder.append(tableName).append(config.getPointChar()).append(config.getOtid()).append(config.getMapEqualitySign()).append(constant);
        firstWordInWhereQueryFlag = false;
    }

    public void appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(String firstTable, String secondTable) {
        if (!firstWordInWhereQueryFlag) {
            appendAnd();
        }
        queryBuilder.append(firstTable).append(config.getPointChar()).append(config.getOtid()).append(config.getEqualitySign()).append(secondTable).append(config.getPointChar()).append(config.getOtid());
        firstWordInWhereQueryFlag = false;
    }

    public void appendWhereConditionWithAttrTypeRefEqualsOB_TY_ID(String attrTypeTable, String ObjectTypeTable) {
        if (!firstWordInWhereQueryFlag) {
            appendAnd();
        }
        queryBuilder.append(attrTypeTable).append(config.getPointChar()).append(config.getOtidref()).append(config.getEqualitySign()).append(ObjectTypeTable).append(config.getPointChar()).append(config.getOtid());
        firstWordInWhereQueryFlag = false;
    }

    public void appendWhereConditionWithTwoTablesEqualsByOB_ID(String firstTable, String secondTable) {
        if (!firstWordInWhereQueryFlag) {
            appendAnd();
        }
        queryBuilder.append(firstTable).append(config.getPointChar()).append(config.getOid()).append(config.getEqualitySign()).append(secondTable).append(config.getPointChar()).append(config.getOid());
        firstWordInWhereQueryFlag = false;
    }

    public void appendWhereConditionWithTwoTablesEqualsByAT_ID(String firstTable, String secondTable) {
        if (!firstWordInWhereQueryFlag) {
            appendAnd();
        }
        queryBuilder.append(firstTable).append(config.getPointChar()).append(config.getAid()).append(config.getEqualitySign()).append(secondTable).append(config.getPointChar()).append(config.getAid());
        firstWordInWhereQueryFlag = false;
    }

    public void appendWhereConditionWithTablesAttrEqualsToValue(String tableName, int value) {
        if (!firstWordInWhereQueryFlag) {
            appendAnd();
        }
        queryBuilder.append(tableName).append(config.getPointChar()).append(config.getAid()).append(config.getEqualitySign()).append(value);
        firstWordInWhereQueryFlag = false;
    }

    public void appendWhereConditionWithTableCodeEqualsToValue(String tableName, String value) {
        if (!firstWordInWhereQueryFlag) {
            appendAnd();
        }
        queryBuilder.append(tableName).append(config.getPointChar()).append(config.getCd()).append(config.getEqualitySign()).append(config.getSingleBracket()).append(value).append(config.getSingleBracket());
        firstWordInWhereQueryFlag = false;
    }

    public void appendWhereConditionWithObRefAttrIdEqualsInteger(String tableName, Integer integer) {
        if (!firstWordInWhereQueryFlag) {
            appendAnd();
        }
        queryBuilder.append(tableName).append(config.getPointChar()).append(config.getAid()).append(config.getEqualitySign()).append(integer);
        firstWordInWhereQueryFlag = false;
    }

    public void appendWhereConditionWithRootTableObjectIdSearching() {
        if (!firstWordInWhereQueryFlag) {
            appendAnd();
        }
        queryBuilder.append(config.getRootTableName()).append(config.getPointChar()).append(config.getOid()).append(config.getMapEqualitySign()).append(config.getEntityIdConstant());
        firstWordInWhereQueryFlag = false;
    }

    public void appendOrderBy(String orderingParameter, boolean ascend) {
        queryBuilder.append("\nORDER BY ").append(orderingParameter);
        if (ascend) {
            queryBuilder.append(" ASC");
        } else queryBuilder.append(" DESC");
    }
}
