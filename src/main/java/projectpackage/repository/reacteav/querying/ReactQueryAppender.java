package projectpackage.repository.reacteav.querying;

import projectpackage.repository.reacteav.support.ReactConstantConfiguration;

/**
 * Created by Gvozd on 08.05.2017.
 */
public class ReactQueryAppender {
    private StringBuilder queryBuilder;
    private ReactConstantConfiguration config;
    private boolean firstWordInSelectQueryFlag = true;
    private boolean firstWordInFromQueryFlag = true;
    private boolean firstWordInWhereQueryFlag = true;
    private boolean queryHasBeenFinished = false;

    public ReactQueryAppender(StringBuilder queryBuilder, ReactConstantConfiguration config) {
        this.queryBuilder = queryBuilder;
        this.config = config;
    }

    public void appendString(String data) {
        queryBuilder.append(data);
    }

    public boolean appendSelectColumnWithNaming(String tableName, String column, String nameOfColumn) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInSelectQueryFlag) appendKoma();
        queryBuilder.append(tableName + config.getPointChar() + column + config.getSpaceChar() + config.getDoubleBracket() + nameOfColumn + config.getDoubleBracket());
        firstWordInSelectQueryFlag = false;
        return true;
    }

    public boolean appendSelectColumnWithValueAndNaming(String tableName, String nameOfColumn) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInSelectQueryFlag) appendKoma();
        queryBuilder.append(tableName + config.getPointChar() + config.getV() + config.getSpaceChar() + config.getDoubleBracket() + nameOfColumn + config.getDoubleBracket());
        firstWordInSelectQueryFlag = false;
        return true;
    }

    public boolean appendSelectColumnWithDataValueAndNaming(String tableName, String nameOfColumn) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInSelectQueryFlag) appendKoma();
        queryBuilder.append(tableName + config.getPointChar() + config.getDv() + config.getSpaceChar() + config.getDoubleBracket() + nameOfColumn + config.getDateAppender() + config.getDoubleBracket());
        firstWordInSelectQueryFlag = false;
        return true;
    }

    public boolean appendSelectWord() {
        if (queryHasBeenFinished) return false;
        queryBuilder.append(config.getSelectWord() + config.getSpaceChar());
        return true;
    }

    public boolean appendFromWord() {
        if (queryHasBeenFinished) return false;
        queryBuilder.append(config.getNewLineChar() + config.getFromWord() + config.getSpaceChar());
        return true;
    }

    public boolean appendWhereWord() {
        if (queryHasBeenFinished) return false;
        queryBuilder.append(config.getNewLineChar() + config.getWhereWord() + config.getSpaceChar());

        return true;
    }

    public boolean appendFromTableWithNaming(String tableName, String tableAlias) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInFromQueryFlag) appendKoma();
        queryBuilder.append(tableName + config.getSpaceChar() + tableAlias);
        firstWordInFromQueryFlag = false;
        return true;
    }

    private void appendKoma() {
        queryBuilder.append(", ");
    }

    private void appendAnd() {
        queryBuilder.append(config.getNewLineAnd());
    }

    public boolean appendWhereConditionWithTableCodeEqualsToConstant(String tableName, String constant) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(tableName + config.getPointChar() + config.getCd() + config.getMapEqualitySign() + constant);
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithTableIdEqualsToConstantInt(String tableName, String constant) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(tableName + config.getPointChar() + config.getOtid() + config.getMapEqualitySign() + constant);
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(String firstTable, String secondTable) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(firstTable + config.getPointChar() + config.getOtid() + config.getEqualitySign() + secondTable + config.getPointChar() + config.getOtid());
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithAttrTypeRefEqualsOB_TY_ID(String attrTypeTable, String ObjectTypeTable) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(attrTypeTable + config.getPointChar() + config.getOtidref() + config.getEqualitySign() + ObjectTypeTable + config.getPointChar() + config.getOtid());
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithTwoTablesEqualsByOB_ID(String firstTable, String secondTable) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(firstTable + config.getPointChar() + config.getOid() + config.getEqualitySign() + secondTable + config.getPointChar() + config.getOid());
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithTwoTablesEqualsByAT_ID(String firstTable, String secondTable) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(firstTable + config.getPointChar() + config.getAid() + config.getEqualitySign() + secondTable + config.getPointChar() + config.getAid());
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithTablesAttrEqualsToValue(String tableName, int value) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(tableName + config.getPointChar() + config.getAid() + config.getEqualitySign() + value);
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithTableCodeEqualsToValue(String tableName, String value) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(tableName + config.getPointChar() + config.getCd() + config.getEqualitySign() + config.getSingleBracket() + value + config.getSingleBracket());
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithObjectIdEqualsSearchingString(String tableName, String searcheable) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(tableName + config.getPointChar() + config.getOid() + config.getEqualitySign() + searcheable);
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithObRefAttrIdEqualsInteger(String tableName, Integer integer) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(tableName + config.getPointChar() + config.getAid() + config.getEqualitySign() + integer);
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithRootTableObjectIdSearching() {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(config.getRootTableName() + config.getPointChar() + config.getOid() + config.getMapEqualitySign() + config.getEntityIdConstant());
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendOrderBy(String orderingParameter, boolean ascend) {
        if (queryHasBeenFinished) return false;
        queryBuilder.append("\nORDER BY " + orderingParameter);
        if (ascend) {
            queryBuilder.append(" ASC");
        } else queryBuilder.append(" DESC");
        queryHasBeenFinished = true;
        closeQueryBuilder();
        return true;
    }

    public void closeQueryBuilder() {
        this.config = null;
        this.queryBuilder = null;
    }
}
