package tests.database.reacteavperformance;

import projectpackage.repository.reacteav.ReactEAVManager;

import java.util.LinkedList;
import java.util.OptionalDouble;

public abstract class PerformanceJob {
    private final String SEPARATOR = "**********************************************************";
    private LinkedList<Long> timings = new LinkedList<>();
    private Long smallestOne=null;
    private Long biggestOne=null;
    ReactEAVManager manager;

    public abstract void doaJob();
    public abstract String getJobName();

    public LinkedList<Long> getTimings() {
        return timings;
    }

    public PerformanceJob(ReactEAVManager manager) {
        this.manager = manager;
    }

    public String getResult(){
        OptionalDouble average = timings.stream().mapToLong((p) -> p).average();
         StringBuilder builder = new StringBuilder(SEPARATOR).append("\n").append(getJobName()).append(" ENDED:\n").append("Smallest time: ").append(smallestOne).append("\n").append("Biggest time: ").append(biggestOne).append("\n").append("Average: ").append(average).append("\n").append("Data: ").append("\n");
         for (Long data:timings) builder.append(data).append("\n");
        return builder.toString();
    }

    void insertResult(Long result){
        if (null == smallestOne){
            smallestOne = result;
        } else if (result<smallestOne){
            smallestOne = result;
        }
        if (null == biggestOne){
            biggestOne = result;
        } else if (result>biggestOne){
            biggestOne = result;
        }
        timings.add(result);
    }
}
