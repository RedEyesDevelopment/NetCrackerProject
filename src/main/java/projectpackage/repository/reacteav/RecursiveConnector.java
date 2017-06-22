package projectpackage.repository.reacteav;

public class RecursiveConnector {
    ReacTask outer;
    ReacTask inner;

    public RecursiveConnector(ReacTask outer, ReacTask inner) {
        this.outer = outer;
        this.inner = inner;
    }

    void doConnect() {
        recursiveConnecting(outer, inner);
    }

    void recursiveConnecting(ReacTask outer, ReacTask inner) {
        if (!inner.getInnerObjects().isEmpty()) {
            for (ReacTask reacTask : inner.getInnerObjects()) {
                recursiveConnecting(inner, reacTask);
            }
        }
        DataInsertor connector = new DataInsertor(outer, inner);
        connector.connectBy();
    }
}
