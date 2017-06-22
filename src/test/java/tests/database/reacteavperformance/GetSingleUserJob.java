package tests.database.reacteavperformance;

import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

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
        User user = null;
        try {
            user = (User) manager.createReactEAV(User.class).getSingleEntityWithId(USERID);
        } catch (ResultEntityNullException e) {
            System.out.println(e);
        }
        assertNotNull(user);
        insertResult(System.currentTimeMillis()-diy);
    }

    @Override
    public String getJobName() {
        return "Simple One User";
    }
}
