package projectpackage.repository.reacteav;

class RecursiveConnector {
    private ReacTask outer;
    private ReacTask inner;

    public RecursiveConnector(ReacTask outer, ReacTask inner) {
        this.outer = outer;
        this.inner = inner;
    }

    void doConnect() {
        recursiveConnecting(outer, inner);
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
