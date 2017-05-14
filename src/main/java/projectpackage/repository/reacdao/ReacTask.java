package projectpackage.repository.reacdao;

import projectpackage.repository.reacdao.fetch.*;
import projectpackage.repository.reacdao.models.ReacEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Gvozd on 06.05.2017.
 */
public class ReacTask {
    private ReactEAV reactEAV;
    private Class<? extends ReacEntity> objectClass;
    private List<ReacEntity> resultList;
    private boolean forSingleObject;
    private Integer targetId;
    private String orderingParameter;
    private boolean ascend;
    private List<ReacTask> innerObjects;
    private ReacEntity entity;
    private LinkedHashMap<String, EntityVariablesData> currentEntityParameters;
    private HashMap<String, EntityOuterRelationshipsData> currentEntityOuterLinks;
    private HashMap<String, EntityReferenceRelationshipsData> currentEntityReferenceRelations;
    private HashMap<String, EntityReferenceTaskData> currentEntityReferenceTasks;
    private HashMap<Integer, EntityReferenceIdRelation> referenceIdRelations;

    ReacTask(ReactEAV reactEAV, Class objectClass, boolean forSingleObject, Integer targetId, String orderingParameter, boolean ascend) {
        this.reactEAV = reactEAV;
        this.objectClass = objectClass;
        this.innerObjects = new LinkedList<>();
        this.resultList = new ArrayList<>();
        this.forSingleObject = forSingleObject;
        this.targetId = targetId;
        this.orderingParameter = orderingParameter;
        this.ascend = ascend;
        this.referenceIdRelations = new HashMap<>();
        this.currentEntityReferenceTasks = new HashMap<>();
        this.referenceIdRelations = new HashMap<>();

        LinkedHashMap<String, EntityVariablesData> currentNodeVariables;
        //Кастуем класс
        entity = null;
        try {
            entity = (ReacEntity) objectClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //Получаем метод getEntityFields
        Method currentObjectClassMethod = null;
        try {
            currentObjectClassMethod = objectClass.getMethod("getEntityFields");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            this.currentEntityParameters = (LinkedHashMap<String, EntityVariablesData>) currentObjectClassMethod.invoke(entity);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();

        }
        try {
            currentObjectClassMethod = objectClass.getMethod("getEntityOuterConnections");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            this.currentEntityOuterLinks = (HashMap<String, EntityOuterRelationshipsData>) currentObjectClassMethod.invoke(entity);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            currentObjectClassMethod = objectClass.getMethod("getEntityReferenceConnections");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            this.currentEntityReferenceRelations = (HashMap<String, EntityReferenceRelationshipsData>) currentObjectClassMethod.invoke(entity);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, EntityReferenceRelationshipsData> getCurrentEntityReferenceRelations() {
        return currentEntityReferenceRelations;
    }

    public void addCurrentEntityReferenceTasks(EntityReferenceTaskData currentEntityReferenceRelation) {
        if (null != currentEntityReferenceRelation) {
            String data = objectClass.getName() + "to" + currentEntityReferenceRelation.getInnerClass();
            this.currentEntityReferenceTasks.put(data, currentEntityReferenceRelation);
        }
    }

    public HashMap<Integer, EntityReferenceIdRelation> getReferenceIdRelations() {
        return referenceIdRelations;
    }

    public void addReferenceIdRelations(int thisId, EntityReferenceIdRelation relation) {
        this.referenceIdRelations.put(thisId,relation);
    }

    public HashMap<String, EntityReferenceTaskData> getCurrentEntityReferenceTasks() {
        return currentEntityReferenceTasks;
    }

    public boolean hasReferencedObjects() {
        return !currentEntityReferenceTasks.isEmpty();
    }

    public ReacEntity getEntity() {
        return entity;
    }

    public Class getObjectClass() {
        return objectClass;
    }

    void setObjectClass(Class<? extends ReacEntity> objectClass) {
        this.objectClass = objectClass;
    }

    LinkedHashMap<String, EntityVariablesData> getCurrentEntityParameters() {
        return currentEntityParameters;
    }

    public List<ReacEntity> getResultList() {
        return resultList;
    }

    void setResultList(List<ReacEntity> resultList) {
        this.resultList = resultList;
    }

    void addResulttoResultList(ReacEntity result) {
        this.resultList.add(result);
    }

    public HashMap<String, EntityOuterRelationshipsData> getCurrentEntityOuterLinks() {
        return currentEntityOuterLinks;
    }

    public List<ReacTask> getInnerObjects() {
        return innerObjects;
    }

    void addInnerObject(ReacTask innerObject) {
        this.innerObjects.add(innerObject);
    }

    public boolean isForSingleObject() {
        return forSingleObject;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public String getOrderingParameter() {
        return orderingParameter;
    }

    public boolean isAscend() {
        return ascend;
    }

    public void setForSingleObject(boolean forSingleObject) {
        this.forSingleObject = forSingleObject;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public void setOrderingParameter(String orderingParameter) {
        this.orderingParameter = orderingParameter;
    }

    public void setAscend(boolean ascend) {
        this.ascend = ascend;
    }

//
//    public ReacTask fetchSingleInnerEntityForInnerObject(Class<? extends ReacEntity> innerEntityClass) {
//        return fetchingOrderCreation(innerEntityClass, true, null, null, false);
//    }

    public ReacTask fetchInnerEntityCollectionForInnerObject(Class<? extends ReacEntity> innerEntityClass) {
        return fetchingOrderCreation(innerEntityClass, false, null, null, false);
    }

    public ReacTask fetchInnerEntityCollectionOrderByForInnerObject(Class<? extends ReacEntity> innerEntityClass, String orderingParameter, boolean ascend) {
        return fetchingOrderCreation(innerEntityClass, false, null, orderingParameter, ascend);
    }

    private ReacTask fetchingOrderCreation(Class<? extends ReacEntity> innerEntityClass, boolean forSingleObject, Integer targetId, String orderingParameter, boolean ascend) {
        ReacTask childNode = new ReacTask(reactEAV, innerEntityClass, forSingleObject, targetId, orderingParameter, ascend);
        childNode.setObjectClass(innerEntityClass);
        this.addInnerObject(childNode);
        return childNode;
    }

    public ReactEAV closeFetch() {
        return reactEAV;
    }
}
