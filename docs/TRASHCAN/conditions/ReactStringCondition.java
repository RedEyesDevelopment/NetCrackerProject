package projectpackage.repository.reacteav.conditions;

/**
 * Created by Lenovo on 20.05.2017.
 */
public class ReactStringCondition {
    private String currentClass;
    private String targetFieldName;
    private String nextClassFieldName;

    public ReactStringCondition(String currentClass, String targetFieldName, String nextClassFieldName) {
        this.currentClass = currentClass;
        this.targetFieldName = targetFieldName;
        this.nextClassFieldName = nextClassFieldName;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public String getTargetFieldName() {
        return targetFieldName;
    }

    public String getNextClassFieldName() {
        return nextClassFieldName;
    }
}
