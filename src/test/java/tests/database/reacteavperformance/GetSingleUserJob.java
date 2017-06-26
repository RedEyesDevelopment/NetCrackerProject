package tests.database.reacteavperformance;

import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.ReactEAVManager;

import static org.junit.Assert.assertNotNull;

public class GetSingleUserJob extends PerformanceJob {
    private long diy;
    private final static int USERID = 901;

    public GetSingleUserJob(ReactEAVManager manager) {
        super(manager);
    }


    @Override
    public void doaJob() {
        diy = System.currentTimeMillis();
        User user = (User) manager.createReactEAV(User.class).getSingleEntityWithId(USERID);
        assertNotNull(user);
        insertResult(System.currentTimeMillis()-diy);
    }

    @Override
    public String getJobName() {
        return "Simple One User";
    }
}
