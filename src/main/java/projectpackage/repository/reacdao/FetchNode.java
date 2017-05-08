package projectpackage.repository.reacdao;

import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.support.EntityVariablesNode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gvozd on 06.05.2017.
 */
public class FetchNode {
    private ReactEAV reactEAV;
    private Class<? extends ReacEntity> objectClass;
    private String orderColumn;
    private boolean orderByColumn;
    private List<FetchNode> nodesList;
    private ReacEntity entity;
    private LinkedHashMap<String, EntityVariablesNode> currentEntityParameters;
    private Map<String, String> tableObjectsMap = new HashMap<>();

    FetchNode(ReactEAV reactEAV) {
        this.reactEAV = reactEAV;
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

    public Map<String, String> getTableObjectsMap() {
        return tableObjectsMap;
    }

    public void setTableObjectsMap(Map<String, String> tableObjectsMap) {
        this.tableObjectsMap = tableObjectsMap;
    }

    List<FetchNode> getNodesList() {
        return nodesList;
    }

    void addFetchedNode(FetchNode nextNode) {
        this.nodesList.add(nextNode);
    }

    public ReactEAV closeFetch() {
        return reactEAV;
    }
}
