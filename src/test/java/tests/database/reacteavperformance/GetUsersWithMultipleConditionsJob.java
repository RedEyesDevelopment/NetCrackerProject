package tests.database.reacteavperformance;

import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.StringWhereCondition;
import projectpackage.repository.reacteav.conditions.VariableWhereCondition;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class GetUsersWithMultipleConditionsJob extends PerformanceJob {
    private long diy;
    private LinkedList<Long> timings = new LinkedList<>();

    public GetUsersWithMultipleConditionsJob(ReactEAVManager manager) {
        super(manager);
    }


    @Override
    public void doaJob() {
        diy = System.currentTimeMillis();
        List<User> users = manager.createReactEAV(User.class).addCondition(new StringWhereCondition("R_REFOB1.REFERENCE=3"), ConditionExecutionMoment.AFTER_APPENDING_WHERE).fetchRootReference(Role.class, "RoleToUser").addCondition(new VariableWhereCondition("roleName", "CLIENT"), ConditionExecutionMoment.AFTER_APPENDING_WHERE).closeAllFetches().getEntityCollection();
        assertNotNull(users);
        insertResult(System.currentTimeMillis()-diy);
    }

    @Override
    public String getJobName() {
        return "Get All Users With Multiple Conditions";
    }
}
