package projectpackage.repository.reacteav;

import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.repository.reacteav.exceptions.WrongFetchException;
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
    private ReacTask parentTask;
    private Class objectClass;
    private String thisClassObjectTypeName;
    private List resultList;
    private boolean forSingleObject;
    private Integer targetId;
    private String orderingParameter;
    private boolean ascend;
    private String referenceId;
    private List<ReacTask> innerObjects;
    private Object entity = null;
    private LinkedHashMap<String, EntityVariablesData> currentEntityParameters;
    private HashMap<Class, EntityOuterRelationshipsData> currentEntityOuterLinks;
    private HashMap<String, EntityReferenceRelationshipsData> currentEntityReferenceRelations;
    private HashMap<Integer, EntityReferenceTaskData> currentEntityReferenceTasks;
    private HashMap<Integer, EntityReferenceIdRelation> referenceIdRelations;

    ReacTask(ReacTask parentTask, ReactEAV reactEAV, Class objectClass, boolean forSingleObject, Integer targetId, String orderingParameter, boolean ascend, String referenceId) {
        this.reactEAV = reactEAV;
        if (null != parentTask) this.parentTask = parentTask;
        this.objectClass = objectClass;
        this.innerObjects = new LinkedList<>();
        this.resultList = new ArrayList<>();
        this.forSingleObject = forSingleObject;
        this.targetId = targetId;
        this.orderingParameter = orderingParameter;
        this.ascend = ascend;
        if (null != referenceId) this.referenceId = referenceId;
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

    void addCurrentEntityReferenceTasks(int thisId, EntityReferenceTaskData currentEntityReferenceRelation) {
        if (null != currentEntityReferenceRelation) {
            this.currentEntityReferenceTasks.put(thisId, currentEntityReferenceRelation);
        }
    }

    HashMap<Integer, EntityReferenceIdRelation> getReferenceIdRelations() {
        return referenceIdRelations;
    }

    void addReferenceIdRelations(Integer okey, EntityReferenceIdRelation value) {
        System.out.println("ENTERING RELATION!");
        this.referenceIdRelations.put(okey, value);
    }

    HashMap<Integer, EntityReferenceTaskData> getCurrentEntityReferenceTasks() {
        return currentEntityReferenceTasks;
    }

    boolean hasReferencedObjects() {
        return !currentEntityReferenceTasks.isEmpty();
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

    HashMap<String, EntityReferenceRelationshipsData> getCurrentEntityReferenceRelations() {
        return currentEntityReferenceRelations;
    }

    List getResultList() {
        return resultList;
    }

    void setResultList(List resultList) {
        this.resultList = resultList;
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

    String getReferenceId() {
        return referenceId;
    }

    public ReacTask fetchInnerChild(Class innerEntityClass) {
        ReacTask newTask = fetchingOrderCreation(innerEntityClass, false, null, null, false, null);
        checkInnerRelations(this.objectClass, newTask.getCurrentEntityOuterLinks().keySet());
        return newTask;
    }

    public ReacTask fetchInnerReference(Class innerEntityClass, String referenceId) {
        ReacTask newTask =fetchingOrderCreation(innerEntityClass, false, null, null, false, null);
        boolean innerHasIt = false;
        for (EntityReferenceRelationshipsData data : newTask.getCurrentEntityReferenceRelations().values()) {
            if (data.getOuterClass().equals(this.objectClass)) {
                innerHasIt = true;
            }
        }
        if (!innerHasIt) {
            WrongFetchException exception = new WrongFetchException(this.objectClass, innerEntityClass, this.toString());
            throw exception;
        }
        return newTask;
    }

    private ReacTask fetchingOrderCreation(Class innerEntityClass, boolean forSingleObject, Integer targetId, String orderingParameter, boolean ascend, String referenceId) {
        validator.isTargetClassAReactEntity(innerEntityClass);
        ReacTask childNode = new ReacTask(this, reactEAV, innerEntityClass, forSingleObject, targetId, orderingParameter, ascend, referenceId);
        this.addInnerObject(childNode);
        return childNode;
    }

    private void checkInnerRelations(Class clazz, Set<Class> set) {
        boolean rootHasIt = false;
        for (Class currentClass : set) {
            if (currentClass.equals(clazz)) rootHasIt = true;
        }
        if (!rootHasIt) {
            WrongFetchException exception = new WrongFetchException(this.objectClass, clazz, this.toString());
            throw exception;
        }
    }

    public ReactEAV closeAllFetches() {
        return reactEAV;
    }

    public ReacTask closeFetch() {
        return parentTask;
    }
}
