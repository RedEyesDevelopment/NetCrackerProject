package tests.database.reacteavperformance;

import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.VariableWhereCondition;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GetUsersWithVariableConditionJob extends PerformanceJob {
    private long diy;
    private LinkedList<Long> timings = new LinkedList<>();

    public GetUsersWithVariableConditionJob(ReactEAVManager manager) {
        super(manager);
    }


    @Override
    public void doaJob() {
        diy = System.currentTimeMillis();
        List<User> users = manager.createReactEAV(User.class).addCondition(new VariableWhereCondition("objectId", "901"), ConditionExecutionMoment.AFTER_APPENDING_WHERE).getEntityCollection();
        assertEquals(1 ,users.size());
        insertResult(System.currentTimeMillis()-diy);
    }

    @Override
    public String getJobName() {
        return "Simple Get All Users With Variable Condition";
    }
}
