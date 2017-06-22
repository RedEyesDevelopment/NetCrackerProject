package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.support.ParentsDAO;
import tests.database.reacteavperformance.*;

import java.util.HashSet;

@Log4j
public class ReactEAVPerformanceTest extends AbstractDatabaseTest {
    private static final int QUERYQUANTITY = 100;

    @Autowired
    ReactEAVManager reactEAVManager;

    @Autowired
    ParentsDAO parentsDAO;

    @Test
    public void queryTestOfUsersOrderBy(){
        long diy;
        HashSet<PerformanceJob> jobs = new HashSet();
        PerformanceJob job1 = new GetUsersJob(reactEAVManager);
        PerformanceJob job2 = new GetSingleUserJob(reactEAVManager);
        PerformanceJob job3 = new GetSingleUserWithInnerJob(reactEAVManager);
        PerformanceJob job4 = new GetUsersWithInnerJob(reactEAVManager);
        PerformanceJob job5 = new GetUsersWithMultipleConditionsJob(reactEAVManager);
        PerformanceJob job6 = new GetUsersWithParentConditionJob(reactEAVManager, parentsDAO);
        PerformanceJob job7 = new GetUsersWithStringConditionJob(reactEAVManager);
        PerformanceJob job8 = new GetUsersWithVariableConditionJob(reactEAVManager);
        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);
        jobs.add(job4);
        jobs.add(job5);
        jobs.add(job6);
        jobs.add(job7);
        jobs.add(job8);

        diy = System.currentTimeMillis();

        for (int i=0; i<=QUERYQUANTITY; i++){
            for (PerformanceJob job:jobs){
                job.doaJob();
            }
        }
        diy = System.currentTimeMillis()-diy;

        System.out.println(SEPARATOR);
        System.out.println("RESULTING TIME="+diy);

        for (PerformanceJob job:jobs){
            System.out.println(job.getResult());
        }
    }
}
