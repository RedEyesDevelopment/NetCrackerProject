package projectpackage.repository.reacdao;

import projectpackage.model.ReacEntity;

import java.util.List;

/**
 * Created by Gvozd on 06.05.2017.
 */
public class FetchNode {
    private ReacDAO reacDAO;
    private Class<? extends ReacEntity> objectClass;
    private String orderColumn;
    private List<FetchNode> nodesList;
    private ReacEntityContainer container;

    FetchNode(ReacDAO parentDao, ReacEntityContainer containerType) {
        this.reacDAO = parentDao;
        this.container = containerType;
    }

    void setContainer(ReacEntityContainer container) {
        this.container = container;
    }

    ReacEntityContainer getContainer() {
        return container;
    }

    Class getObjectClass() {
        return objectClass;
    }

    void setObjectClass(Class<? extends ReacEntity> objectClass) {
        this.objectClass = objectClass;
    }

    List<FetchNode> getNodesList() {
        return nodesList;
    }

    void addFetchedNode(FetchNode nextNode) {
        this.nodesList.add(nextNode);
    }

    public FetchNode returnOrderedBy(String orderColumn){
        if (!container.equals(ReacEntityContainer.SINGLE_OBJECT)) {
            this.orderColumn = orderColumn;
            return this;
        } else {
            throw new IllegalStateException("Trying to order single-target inner entity "+objectClass+" from "+reacDAO);
        }
    }

    public ReacDAO closeFetch() {
        return reacDAO;
    }
}
