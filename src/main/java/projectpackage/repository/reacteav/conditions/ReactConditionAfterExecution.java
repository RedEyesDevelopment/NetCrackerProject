package projectpackage.repository.reacteav.conditions;

import java.util.List;

public interface ReactConditionAfterExecution extends ReactCondition {
    public Class getTargetClass();
    public void loadDataToParse(List<Object> data);
}
