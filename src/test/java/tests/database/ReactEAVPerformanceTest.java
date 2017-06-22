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
    private final String SEPARATOR = "**********************************************************";

    @Autowired
    ReactEAVManager manager;

    @Autowired
    ParentsDAO parentsDAO;

    @Test
    public void queryTestOfUsersOrderBy(){
        long diy;
        HashSet<PerformanceJob> jobs = new HashSet();
        PerformanceJob job1 = new GetUsersJob(manager);
        PerformanceJob job2 = new GetSingleUserJob(manager);
        PerformanceJob job3 = new GetSingleUserWithInnerJob(manager);
        PerformanceJob job4 = new GetUsersWithInnerJob(manager);
        PerformanceJob job5 = new GetUsersWithMultipleConditionsJob(manager);
        PerformanceJob job6 = new GetUsersWithParentConditionJob(manager, parentsDAO);
        PerformanceJob job7 = new GetUsersWithStringConditionJob(manager);
        PerformanceJob job8 = new GetUsersWithVariableConditionJob(manager);
        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);
        jobs.add(job4);
        jobs.add(job5);
        jobs.add(job6);
        jobs.add(job7);
        jobs.add(job8);

        diy = System.currentTimeMillis();

        for (int i=0; i<10; i++){
            for (PerformanceJob job:jobs){
                job.doaJob();
            }
        }
        diy = System.currentTimeMillis()-diy;

        System.out.println("RESULTING TIME="+diy);

        for (PerformanceJob job:jobs){
            System.out.println(job.getResult());
        }
    }
}
