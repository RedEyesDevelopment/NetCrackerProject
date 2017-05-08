package projectpackage.repository.reacdao.support;

import projectpackage.repository.reacdao.ReacTask;
import projectpackage.repository.reacdao.models.ReacEntity;

import java.util.List;

/**
 * Created by Lenovo on 08.05.2017.
 */
public class ReacResultDataConnector {
    private ReacTask rootTask;

    public ReacResultDataConnector(ReacTask rootTask) {
        this.rootTask = rootTask;
    }

    public List<ReacEntity> connectEntitiesAndReturn() {
        for (ReacTask reacTask : rootTask.getInnerObjects()) {
            recursiveConnecting(rootTask, reacTask);
        }
        return rootTask.getResultList();
    }

    private void recursiveConnecting(ReacTask outer, ReacTask inner) {
        if (!inner.getInnerObjects().isEmpty()) {
            for (ReacTask reacTask : inner.getInnerObjects()) {
                recursiveConnecting(inner, reacTask);
            }
        }
        DataInsertor connector = new DataInsertor(outer, inner);
        connector.connectBy();
    }


}
