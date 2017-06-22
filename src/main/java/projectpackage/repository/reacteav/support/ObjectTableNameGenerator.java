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

    public String getCurrentTableName() {
        return new StringBuilder(tableName).append(counter).toString();
    }

    public int getTablesCounter() {
        return counter;
    }
}
