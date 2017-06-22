package tests.database.reacteavperformance;

import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.support.ParentsDAO;

import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;

public class GetUsersWithParentConditionJob extends PerformanceJob {
    ParentsDAO parentsDAO;
    private long diy;
    private LinkedList<Long> timings = new LinkedList<>();

    public GetUsersWithParentConditionJob(ReactEAVManager manager, ParentsDAO parentsDAO) {
        super(manager);
        this.parentsDAO = parentsDAO;
    }

    @Override
    public void doaJob() {
        diy = System.currentTimeMillis();
        Integer parentId = parentsDAO.getParentId(1101);
        User user = null;
        try {
            user = (User) manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeAllFetches().getSingleEntityWithId(parentId);
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
        assertNotNull(user);
        insertResult(System.currentTimeMillis()-diy);
    }

    @Override
    public String getJobName() {
        return "Get All Users With Parent Condition";
    }
}
