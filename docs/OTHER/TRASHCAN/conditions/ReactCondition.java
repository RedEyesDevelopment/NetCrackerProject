package projectpackage.repository.reacteav.conditions;

public class ReactCondition {
    private Class currentObjectClass;
    private String currentTargetField;
    private String currentObjectField;
    private ReactCondition previousCondition;
    private ReactCondition nextCondition;

    public ReactCondition(Class currentObjectClass, String currentTargetField, String currentObjectField, ReactCondition previousCondition) {
        this.currentObjectClass = currentObjectClass;
        this.currentTargetField = currentTargetField;
        this.currentObjectField = currentObjectField;
        this.previousCondition = previousCondition;
    }

    public Class getCurrentObjectClass() {
        return currentObjectClass;
    }

    public String getCurrentTargetField() {
        return currentTargetField;
    }

    public String getCurrentObjectField() {
        return currentObjectField;
    }

    public ReactCondition getPreviousCondition() {
        return previousCondition;
    }

    public ReactCondition getNextCondition() {
        return nextCondition;
    }

    public void setNextCondition(ReactCondition nextCondition) {
        this.nextCondition = nextCondition;
    }
}
