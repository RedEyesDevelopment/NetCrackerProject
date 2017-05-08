package projectpackage.repository.reacdao;

import projectpackage.repository.reacdao.fetch.EntityInnerObjectNode;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.fetch.EntityVariablesNode;

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
    private LinkedHashMap<String, EntityVariablesNode> currentEntityParameters;
    private LinkedHashMap<String, EntityInnerObjectNode> currentEntityInnerObjects;

    ReacTask(ReactEAV reactEAV, Class objectClass, boolean forSingleObject, Integer targetId, String orderingParameter, boolean ascend) {
        this.reactEAV = reactEAV;
        this.objectClass = objectClass;
        this.innerObjects = new ArrayList<>();
        this.resultList = new ArrayList<>();
        this.forSingleObject = forSingleObject;
        this.targetId = targetId;
        this.orderingParameter = orderingParameter;
        this.ascend = ascend;

        LinkedHashMap<String, EntityVariablesNode> currentNodeVariables;
//        Кастуем класс
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
            this.currentEntityParameters = (LinkedHashMap<String, EntityVariablesNode>) currentObjectClassMethod.invoke(entity);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }try {
            currentObjectClassMethod = objectClass.getMethod("getEntityInnerObjects");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            this.currentEntityInnerObjects = (LinkedHashMap<String, EntityInnerObjectNode>) currentObjectClassMethod.invoke(entity);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public ReacEntity getEntity() {
        return entity;
    }

    Class getObjectClass() {
        return objectClass;
    }

    void setObjectClass(Class<? extends ReacEntity> objectClass) {
        this.objectClass = objectClass;
    }

    LinkedHashMap<String, EntityVariablesNode> getCurrentEntityParameters() {
        return currentEntityParameters;
    }

    List<ReacEntity> getResultList() {
        return resultList;
    }

    void setResultList(List<ReacEntity> resultList) {
        this.resultList = resultList;
    }

    void addResulttoResultList(ReacEntity result){
        this.resultList.add(result);
    }

    LinkedHashMap<String, EntityInnerObjectNode> getCurrentEntityInnerObjects() {
        return currentEntityInnerObjects;
    }

    List<ReacTask> getInnerObjects() {
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

    public ReacTask fetchSingleInnerEntityForInnerObject(Class<? extends ReacEntity> innerEntityClass){
        return fetchingOrderCreation(innerEntityClass,true,null,null,false);
    }

    public ReacTask fetchInnerEntityCollectionForInnerObject(Class<? extends ReacEntity> innerEntityClass){
        return fetchingOrderCreation(innerEntityClass,false,null,null,false);
    }

    public ReacTask fetchInnerEntityCollectionOrderByForInnerObject(Class<? extends ReacEntity> innerEntityClass, String orderingParameter, boolean ascend){
        return fetchingOrderCreation(innerEntityClass,false,null,orderingParameter,ascend);
}

    private ReacTask fetchingOrderCreation(Class<? extends ReacEntity> innerEntityClass,boolean forSingleObject, Integer targetId, String orderingParameter, boolean ascend){
        ReacTask childNode = new ReacTask(reactEAV, innerEntityClass, forSingleObject, targetId,orderingParameter,ascend);
        childNode.setObjectClass(innerEntityClass);
        this.addInnerObject(childNode);
        return childNode;
    }

    public ReactEAV closeFetch() {
        return reactEAV;
    }
}
