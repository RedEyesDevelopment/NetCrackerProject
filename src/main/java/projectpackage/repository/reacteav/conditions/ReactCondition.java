package projectpackage.repository.reacteav.conditions;

public interface ReactCondition {
    public Class getNeededConditionExecutor();
    public void execute();
}
