package projectpackage.repository.reacdao;

import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.support.EntityVariablesNode;
import projectpackage.repository.reacdao.support.ReactResultQuantityType;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Gvozd on 06.05.2017.
 */
public class FetchNode {
    private ReactEAV reactEAV;
    private Class<? extends ReacEntity> objectClass;
    private String orderColumn;
    private boolean orderByColumn;
    private List<FetchNode> nodesList;
    private ReactResultQuantityType container;
    private ReacEntity entity;
    private LinkedHashMap<String, EntityVariablesNode> currentEntityParameters;

    FetchNode(ReactEAV reactEAV, ReactResultQuantityType containerType) {
        this.reactEAV = reactEAV;
        this.container = containerType;
    }

    public FetchNode(ReactEAV reactEAV) {
        this.reactEAV = reactEAV;
    }

    void setContainer(ReactResultQuantityType container) {
        this.container = container;
    }

    ReactResultQuantityType getContainer() {
        return container;
    }

    Class getObjectClass() {
        return objectClass;
    }

    void setObjectClass(Class<? extends ReacEntity> objectClass) {
        this.objectClass = objectClass;
    }

    public ReacEntity getEntity() {
        return entity;
    }

    public void setEntity(ReacEntity entity) {
        this.entity = entity;
    }

    public LinkedHashMap<String, EntityVariablesNode> getCurrentEntityParameters() {
        return currentEntityParameters;
    }

    public void setCurrentEntityParameters(LinkedHashMap<String, EntityVariablesNode> currentEntityParameters) {
        this.currentEntityParameters = currentEntityParameters;
    }

    List<FetchNode> getNodesList() {
        return nodesList;
    }

    void addFetchedNode(FetchNode nextNode) {
        this.nodesList.add(nextNode);
    }

    public FetchNode returnOrderedByColumn(String orderColumn){
        if (!container.equals(ReactResultQuantityType.SINGLE_OBJECT)) {
            this.orderColumn = orderColumn;
            orderByColumn = true;
            return this;
        } else {
            throw new IllegalStateException("Trying to order single-target inner entity "+objectClass+" from "+ reactEAV);
        }
    }

    public FetchNode returnOrderedByParameter(String orderParameter){
        if (!container.equals(ReactResultQuantityType.SINGLE_OBJECT)) {
            this.orderColumn = orderParameter;
            orderByColumn = false;
            return this;
        } else {
            throw new IllegalStateException("Trying to order single-target inner entity "+objectClass+" from "+ reactEAV);
        }
    }

    public ReactEAV closeFetch() {
        return reactEAV;
    }
}
