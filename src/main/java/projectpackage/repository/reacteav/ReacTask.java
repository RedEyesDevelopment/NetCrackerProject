package projectpackage.repository.reacteav;

import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.ReactCondition;
import projectpackage.repository.reacteav.conditions.ReactConditionData;
import projectpackage.repository.reacteav.exceptions.WrongFetchException;
import projectpackage.repository.reacteav.relationsdata.*;
import projectpackage.repository.reacteav.support.ReactEntityValidator;

import java.util.*;

public class ReacTask {

    @Autowired
    private ReactEntityValidator validator = new ReactEntityValidator();
    private ReactEAV reactEAV;
    private ReacTask parentTask;
    private Class objectClass;
    private int thisClassObjectTypeName;
    private List resultList;
    private boolean forSingleObject;
    private Integer targetId;
    private String orderingParameter;
    private boolean ascend;
    private String referenceId;
    private List<ReacTask> innerObjects;
    private List<Integer> idsListForChildFetchesOfInnerEntities;
    private List<Integer> parentalIdsForChildFetch;
    private Set<Integer> objectIdsForReferenceFetch;
    private LinkedHashMap<String, EntityVariablesData> currentEntityParameters;
    private HashMap<Class, EntityOuterRelationshipsData> currentEntityOuterLinks;
    private HashMap<String, EntityReferenceRelationshipsData> currentEntityReferenceRelations;
    private HashMap<Integer, EntityReferenceTaskData> currentEntityReferenceTasks;
    private HashMap<Integer, EntityReferenceIdRelation> referenceIdRelations;

    ReacTask(ReacTask parentTask, ReactEAV reactEAV, Class objectClass, boolean forSingleObject, Integer targetId, String orderingParameter, boolean ascend, String referenceId) {
        this.reactEAV = reactEAV;
        if (null != parentTask) {
            this.parentTask = parentTask;
        }
        this.objectClass = objectClass;
        this.innerObjects = new LinkedList<>();
        this.resultList = new ArrayList<>();
        this.forSingleObject = forSingleObject;
        this.targetId = targetId;
        this.orderingParameter = orderingParameter;
        this.ascend = ascend;
        if (null != referenceId) {
            this.referenceId = referenceId;
        }
        this.referenceIdRelations = new HashMap<>();
        this.currentEntityReferenceTasks = new HashMap<>();
        this.idsListForChildFetchesOfInnerEntities =new ArrayList<>(6);

        this.currentEntityParameters = reactEAV.getDataBucket().getEntityVariablesMap().get(objectClass);
        if (null == currentEntityParameters) {
            currentEntityParameters = new LinkedHashMap<>();
        }

        this.currentEntityOuterLinks = reactEAV.getDataBucket().getOuterRelationsMap().get(objectClass);
        if (null == currentEntityOuterLinks) {
            currentEntityOuterLinks = new HashMap<>();
        }

        this.currentEntityReferenceRelations = reactEAV.getDataBucket().getEntityReferenceRelationsMap().get(objectClass);
        if (null == currentEntityReferenceRelations) {
            currentEntityReferenceRelations = new LinkedHashMap<>();
        }

        this.thisClassObjectTypeName = reactEAV.getDataBucket().getClassesMap().get(objectClass);
    }

    void addCurrentEntityReferenceTasks(int thisId, EntityReferenceTaskData currentEntityReferenceRelation) {
        if (null != currentEntityReferenceRelation) {
            this.currentEntityReferenceTasks.put(thisId, currentEntityReferenceRelation);
        }
    }

    List<Integer> getParentalIdsForChildFetch() {
        return parentalIdsForChildFetch;
    }

    Set<Integer> getObjectIdsForReferenceFetch() {
        return objectIdsForReferenceFetch;
    }

    void manageParentAndReferenceLists() {
        this.parentalIdsForChildFetch = null;
        if (null!=parentTask) {
            for (Class clazz:currentEntityOuterLinks.keySet()){
                if (clazz.equals(parentTask.getObjectClass())){
                    this.parentalIdsForChildFetch = parentTask.getIdsListForChildFetchesOfInnerEntities();
                }
            }
            this.objectIdsForReferenceFetch = null;
            if (!parentTask.getReferenceIdRelations().isEmpty()){
                for (EntityReferenceIdRelation parentRelation : parentTask.getReferenceIdRelations().values()) {
                    if (parentRelation.getInnerClass().equals(this.getObjectClass())) {
                        if (null == objectIdsForReferenceFetch) {
                            objectIdsForReferenceFetch = new HashSet<>();
                        }
                        this.objectIdsForReferenceFetch.add(parentRelation.getOuterId());
                    }
                }
            }
        }
    }

    private List<Integer> getIdsListForChildFetchesOfInnerEntities() {
        return idsListForChildFetchesOfInnerEntities;
    }

    void addIdForChildFetches(Integer newObjectId) {
        if (null!= newObjectId) {
            this.idsListForChildFetchesOfInnerEntities.add(newObjectId);
        }
    }

    HashMap<Integer, EntityReferenceIdRelation> getReferenceIdRelations() {
        return referenceIdRelations;
    }

    void addReferenceIdRelations(Integer key, EntityReferenceIdRelation value) {
        this.referenceIdRelations.put(key, value);
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

    int getThisClassObjectTypeName() {
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

    public ReacTask addCondition(ReactCondition condition, ConditionExecutionMoment moment) {
        ReactConditionData data = new ReactConditionData(condition, this, moment);
        reactEAV.generateCondition(data);
        return this;
    }

    public ReacTask fetchInnerChild(Class innerEntityClass) {
        ReacTask newTask = fetchingOrderCreation(innerEntityClass, null);
        checkInnerRelations(this.objectClass, newTask.getCurrentEntityOuterLinks().keySet());
        return newTask;
    }

    public ReacTask fetchInnerReference(Class innerEntityClass, String referenceId) {
        ReacTask newTask =fetchingOrderCreation(innerEntityClass, referenceId);
        boolean innerHasIt = false;
        for (EntityReferenceRelationshipsData data : newTask.getCurrentEntityReferenceRelations().values()) {
            if (data.getOuterClass().equals(this.objectClass)) {
                innerHasIt = true;
            }
        }
        if (!innerHasIt) {
            throw new WrongFetchException(this.objectClass, innerEntityClass, this.toString());
        }
        return newTask;
    }

    private ReacTask fetchingOrderCreation(Class innerEntityClass, String referenceId) {
        validator.isTargetClassAReactEntity(innerEntityClass);
        ReacTask childNode = new ReacTask(this, reactEAV, innerEntityClass, false, null, null, false, referenceId);
        this.addInnerObject(childNode);
        return childNode;
    }

    private void checkInnerRelations(Class clazz, Set<Class> set) {
        boolean rootHasIt = false;
        for (Class currentClass : set) {
            if (currentClass.equals(clazz)) {
                rootHasIt = true;
            }
        }
        if (!rootHasIt) {
            throw new WrongFetchException(this.objectClass, clazz, this.toString());
        }
    }

    public ReactEAV closeAllFetches() {
        return reactEAV;
    }

    public ReacTask closeFetch() {
        return parentTask;
    }
}
