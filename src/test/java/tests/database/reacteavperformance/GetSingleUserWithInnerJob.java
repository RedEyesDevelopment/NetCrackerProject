package tests.database.reacteavperformance;

import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetSingleUserWithInnerJob extends PerformanceJob {
    private long diy;
    private final static int USERID = 901;
    private LinkedList<Long> timings = new LinkedList<>();

    public GetSingleUserWithInnerJob(ReactEAVManager manager) {
        super(manager);
    }


    @Override
    public void doaJob() {
        diy = System.currentTimeMillis();
        User user = null;
        try {
            user = (User) manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeAllFetches().fetchRootReference(Role.class, "RoleToUser").closeAllFetches().getSingleEntityWithId(USERID);
        } catch (ResultEntityNullException e) {
            System.out.println(e);
        }
        assertNotNull(user);
        assertNotNull(user.getRole());
        assertEquals(1, user.getPhones());
        insertResult(System.currentTimeMillis()-diy);
    }

    @Override
    public String getJobName() {
        return "One User With Inners";
    }
}
