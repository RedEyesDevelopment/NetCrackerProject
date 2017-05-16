package projectpackage.repository.reacteav;

import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.repository.reacteav.relationsdata.*;
import projectpackage.repository.reacteav.support.ReactEntityValidator;

import java.util.*;

/**
 * Created by Gvozd on 06.05.2017.
 */
public class ReacTask {

    @Autowired
    ReactEntityValidator validator = new ReactEntityValidator();
    private ReactEAV reactEAV;
    private Class objectClass;
    private String thisClassObjectTypeName;
    private List resultList;
    private boolean forSingleObject;
    private Integer targetId;
    private String orderingParameter;
    private boolean ascend;
    private List<ReacTask> innerObjects;
    private Object entity = null;
    private LinkedHashMap<String, EntityVariablesData> currentEntityParameters;
    private HashMap<Class, EntityOuterRelationshipsData> currentEntityOuterLinks;
    private HashMap<Class, EntityReferenceRelationshipsData> currentEntityReferenceRelations;
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

        //Кастуем класс
        try {
            entity = objectClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.currentEntityParameters = reactEAV.getDataBucket().getEntityVariablesMap().get(objectClass);
        if (null == currentEntityParameters) currentEntityParameters = new LinkedHashMap<>();
        this.currentEntityOuterLinks = reactEAV.getDataBucket().getOuterRelationsMap().get(objectClass);
        if (null == currentEntityOuterLinks) currentEntityOuterLinks = new HashMap<>();
        this.currentEntityReferenceRelations = reactEAV.getDataBucket().getEntityReferenceRelationsMap().get(objectClass);
        if (null == currentEntityReferenceRelations) currentEntityReferenceRelations = new LinkedHashMap<>();
        this.thisClassObjectTypeName = reactEAV.getDataBucket().getClassesMap().get(objectClass);
    }

    void addCurrentEntityReferenceTasks(EntityReferenceTaskData currentEntityReferenceRelation) {
        if (null != currentEntityReferenceRelation) {
            String data = objectClass.getName() + "to" + currentEntityReferenceRelation.getInnerClass();
            this.currentEntityReferenceTasks.put(data, currentEntityReferenceRelation);
        }
    }

    HashMap<Integer, EntityReferenceIdRelation> getReferenceIdRelations() {
        return referenceIdRelations;
    }

    void addReferenceIdRelations(int thisId, EntityReferenceIdRelation relation) {
        this.referenceIdRelations.put(thisId, relation);
    }

    HashMap<String, EntityReferenceTaskData> getCurrentEntityReferenceTasks() {
        return currentEntityReferenceTasks;
    }

    boolean hasReferencedObjects() {
        return !currentEntityReferenceTasks.isEmpty();
    }

    Object getEntity() {
        return entity;
    }

    Class getObjectClass() {
        return objectClass;
    }

    void setObjectClass(Class objectClass) {
        this.objectClass = objectClass;
    }

    String getThisClassObjectTypeName() {
        return thisClassObjectTypeName;
    }

    LinkedHashMap<String, EntityVariablesData> getCurrentEntityParameters() {
        return currentEntityParameters;
    }

    HashMap<Class, EntityOuterRelationshipsData> getCurrentEntityOuterLinks() {
        return currentEntityOuterLinks;
    }

    HashMap<Class, EntityReferenceRelationshipsData> getCurrentEntityReferenceRelations() {
        return currentEntityReferenceRelations;
    }

    List getResultList() {
        return resultList;
    }

    void setResultList(List resultList) {
        this.resultList = resultList;
    }

    void addResulttoResultList(Object result) {
        this.resultList.add(result);
    }

    List<ReacTask> getInnerObjects() {
        return innerObjects;
    }

    void addInnerObject(ReacTask innerObject) {
        this.innerObjects.add(innerObject);
    }

    boolean isForSingleObject() {
        return forSingleObject;
    }

    Integer getTargetId() {
        return targetId;
    }

    String getOrderingParameter() {
        return orderingParameter;
    }

    boolean isAscend() {
        return ascend;
    }

    void setForSingleObject(boolean forSingleObject) {
        this.forSingleObject = forSingleObject;
    }

    void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    void setOrderingParameter(String orderingParameter) {
        this.orderingParameter = orderingParameter;
    }

    void setAscend(boolean ascend) {
        this.ascend = ascend;
    }

    public ReacTask fetchInnerEntityCollectionForInnerObject(Class innerEntityClass) {
        return fetchingOrderCreation(innerEntityClass, false, null, null, false);
    }

    public ReacTask fetchInnerEntityCollectionOrderByForInnerObject(Class innerEntityClass, String orderingParameter, boolean ascend) {
        return fetchingOrderCreation(innerEntityClass, false, null, orderingParameter, ascend);
    }

    private ReacTask fetchingOrderCreation(Class innerEntityClass, boolean forSingleObject, Integer targetId, String orderingParameter, boolean ascend) {
        validator.isTargetClassAReactEntity(innerEntityClass);
        ReacTask childNode = new ReacTask(reactEAV, innerEntityClass, forSingleObject, targetId, orderingParameter, ascend);
        this.addInnerObject(childNode);
        return childNode;
    }

    public ReactEAV closeFetch() {
        return reactEAV;
    }
}
