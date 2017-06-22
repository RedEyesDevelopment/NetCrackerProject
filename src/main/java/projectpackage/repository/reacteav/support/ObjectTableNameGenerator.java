package projectpackage.repository.reacteav.support;

public class ObjectTableNameGenerator {
    private String tableName;
    private int counter;

    public ObjectTableNameGenerator(String tableName) {
        this.tableName = tableName;
        this.counter = 0;
    }

    public String getNextTableName() {
        counter++;
        return getCurrentTableName();
    }

    private String getCurrentTableName() {
        return tableName + counter;
    }

    public int getTablesCounter() {
        return counter;
    }
}
