package tests.database.reacteavperformance;

import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetUsersWithInnerJob extends PerformanceJob {
    private long diy;
    private LinkedList<Long> timings = new LinkedList<>();

    public GetUsersWithInnerJob(ReactEAVManager manager) {
        super(manager);
    }

    @Override
    public void doaJob() {
        diy = System.currentTimeMillis();
        List<User> list = null;
        System.out.println(manager);
        try {
            list = (List<User>) manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeAllFetches().fetchRootReference(Role.class, "RoleToUser").closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            System.out.println(e);
        }
        assertEquals(4 ,list.size());
        for (User user:list){
            assertNotNull(user);
            assertNotNull(user.getRole());
            if (user.getObjectId()!=999) assertNotNull(user.getPhones());
        }
        insertResult(System.currentTimeMillis()-diy);
    }

    @Override
    public String getJobName() {
        return "Simple Get All Users";
    }
}
