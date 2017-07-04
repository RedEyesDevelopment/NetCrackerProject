package tests.database.reacteavperformance;

import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.ReactEAVManager;

import java.util.LinkedList;

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
        User user = (User) manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeAllFetches().fetchRootReference(Role.class, "RoleToUser").closeAllFetches().getSingleEntityWithId(USERID);
        assertNotNull(user);
        assertNotNull(user.getRole());
        if (user.getObjectId()!=999) {
            assertNotNull(user.getPhones());
        }
        insertResult(System.currentTimeMillis()-diy);
    }

    @Override
    public String getJobName() {
        return "One User With Inners";
    }
}
