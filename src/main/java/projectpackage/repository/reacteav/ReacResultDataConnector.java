package projectpackage.repository.reacteav;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Lenovo on 08.05.2017.
 */
public class ReacResultDataConnector {
    private ReacTask rootTask;

    public ReacResultDataConnector(ReacTask rootTask) {
        this.rootTask = rootTask;
    }

    public List connectEntitiesAndReturn() {
        List<RecursiveConnector> connectors = new ArrayList<>();
        for (ReacTask reacTask : rootTask.getInnerObjects()) {
            RecursiveConnector connector = new RecursiveConnector(rootTask, reacTask);
            connectors.add(connector);
        }
        ListIterator<RecursiveConnector> iterator = connectors.listIterator(connectors.size());
        while(iterator.hasPrevious()){
            RecursiveConnector connector = iterator.previous();
            connector.doConnect();
        }
//        for (RecursiveConnector connector : connectors.) connector.doConnect();

        return rootTask.getResultList();
    }

}

