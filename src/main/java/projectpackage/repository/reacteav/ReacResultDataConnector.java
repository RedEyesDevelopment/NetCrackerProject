package projectpackage.repository.reacteav;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

class ReacResultDataConnector {
    private ReacTask rootTask;

    ReacResultDataConnector(ReacTask rootTask) {
        this.rootTask = rootTask;
    }

    List connectEntitiesAndReturn() {
        List<RecursiveConnector> connectors = new ArrayList<>();
        for (ReacTask reacTask : rootTask.getInnerObjects()) {
            RecursiveConnector connector = new RecursiveConnector(rootTask, reacTask);
            connectors.add(connector);
        }
        ListIterator<RecursiveConnector> iterator = connectors.listIterator(connectors.size());
        while (iterator.hasPrevious()) {
            RecursiveConnector connector = iterator.previous();
            connector.doConnect();
        }
        return rootTask.getResultList();
    }

}

