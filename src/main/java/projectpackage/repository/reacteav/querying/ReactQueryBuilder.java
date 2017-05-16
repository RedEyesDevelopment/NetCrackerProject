package projectpackage.repository.reacteav.querying;

import projectpackage.repository.reacteav.support.ReactConstantConfiguration;

/**
 * Created by Gvozd on 08.05.2017.
 */
public class ReactQueryBuilder {
    private StringBuilder queryBuilder;
    private ReactConstantConfiguration config;
    private boolean firstWordInSelectQueryFlag = true;
    private boolean firstWordInFromQueryFlag = true;
    private boolean firstWordInWhereQueryFlag = true;
    private boolean queryHasBeenFinished = false;

    public ReactQueryBuilder(StringBuilder queryBuilder, ReactConstantConfiguration config) {
        this.queryBuilder = queryBuilder;
        this.config = config;
    }

    public void appendString(String data) {
        queryBuilder.append(data);
    }

    public boolean appendSelectColumnWithNaming(String tableName, String column, String nameOfColumn) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInSelectQueryFlag) appendKoma();
        queryBuilder.append(tableName + "." + column + " \"" + nameOfColumn + "\"");
        firstWordInSelectQueryFlag = false;
        return true;
    }

    public boolean appendSelectColumnWithValueAndNaming(String tableName, String nameOfColumn) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInSelectQueryFlag) appendKoma();
        queryBuilder.append(tableName + "." + config.getV() + " \"" + nameOfColumn + "\"");
        firstWordInSelectQueryFlag = false;
        return true;
    }

    public boolean appendSelectColumnWithDataValueAndNaming(String tableName, String nameOfColumn) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInSelectQueryFlag) appendKoma();
        queryBuilder.append(tableName + "." + config.getDv() + " \"" + nameOfColumn + config.getDateAppender() + "\"");
        firstWordInSelectQueryFlag = false;
        return true;
    }

    public boolean appendSelectWord() {
        if (queryHasBeenFinished) return false;
        queryBuilder.append("SELECT ");
        return true;
    }

    public boolean appendFromWord() {
        if (queryHasBeenFinished) return false;
        queryBuilder.append("\nFROM ");
        return true;
    }

    public boolean appendWhereWord() {
        if (queryHasBeenFinished) return false;
        queryBuilder.append("\nWHERE ");
        return true;
    }

    public boolean appendFromTableWithNaming(String tableName, String tableAlias) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInFromQueryFlag) appendKoma();
        queryBuilder.append(tableName + " " + tableAlias);
        firstWordInFromQueryFlag = false;
        return true;
    }

    private void appendKoma() {
        queryBuilder.append(", ");
    }

    private void appendAnd() {
        queryBuilder.append("\nAND ");
    }

    public boolean appendWhereConditionWithTableCodeEqualsToConstant(String tableName, String constant) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(tableName + "." + config.getCd() + "=:" + constant);
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(String firstTable, String secondTable) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(firstTable + "." + config.getOtid() + "=" + secondTable + "." + config.getOtid());
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithAttrTypeRefEqualsOB_TY_ID(String attrTypeTable, String ObjectTypeTable) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(attrTypeTable + "." + config.getOtidref() + "=" + ObjectTypeTable + "." + config.getOtid());
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithTwoTablesEqualsByOB_ID(String firstTable, String secondTable) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(firstTable + "." + config.getOid() + "=" + secondTable + "." + config.getOid());
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithTwoTablesEqualsByAT_ID(String firstTable, String secondTable) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(firstTable + "." + config.getAid() + "=" + secondTable + "." + config.getAid());
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithTableCodeEqualsToValue(String tableName, String value) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(tableName + "." + config.getCd() + "='" + value + "'");
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithObjectIdEqualsSearchingString(String tableName, String searcheable) {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(tableName + "." + config.getOid() + "=" + searcheable);
        firstWordInWhereQueryFlag = false;
        return true;
    }

    public boolean appendWhereConditionWithRootTableObjectIdSearching() {
        if (queryHasBeenFinished) return false;
        if (!firstWordInWhereQueryFlag) appendAnd();
        queryBuilder.append(config.getRootTableName() + "." + config.getOid() + "= :" + config.getEntityIdConstant());
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
