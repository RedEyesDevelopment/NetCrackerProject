package projectpackage.repository.reacteav.conditions;

import java.util.List;

/**
 * Created by Lenovo on 20.05.2017.
 */
public interface ReactCondition {
    public Class getTargetClass();
    public void loadDataToParse(List<Object> data);
    public void execute();
}
