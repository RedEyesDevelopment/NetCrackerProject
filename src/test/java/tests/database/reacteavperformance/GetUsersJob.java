package tests.database.reacteavperformance;

import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GetUsersJob extends PerformanceJob {
    private long diy;
    private LinkedList<Long> timings = new LinkedList<>();

    public GetUsersJob(ReactEAVManager manager) {
        super(manager);
    }


    @Override
    public void doaJob() {
        diy = System.currentTimeMillis();
        List<User> list = null;
        try {
            list = (List<User>) manager.createReactEAV(User.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            System.out.println(e);
        }
        assertEquals(4 ,list.size());
        insertResult(System.currentTimeMillis()-diy);
    }

    @Override
    public String getJobName() {
        return "Simple Get All Users";
    }
}
