package projectpackage.repository.reacteav;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.ConditionExecutor;
import projectpackage.repository.reacteav.conditions.ReactCondition;
import projectpackage.repository.reacteav.conditions.ReactConditionData;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.reacteav.exceptions.WrongFetchException;
import projectpackage.repository.reacteav.querying.ReactQueryTaskHolder;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceTaskData;
import projectpackage.repository.reacteav.relationsdata.EntityVariablesData;
import projectpackage.repository.reacteav.support.ReactConnectionsDataBucket;
import projectpackage.repository.reacteav.support.ReactConstantConfiguration;
import projectpackage.repository.reacteav.support.ReactEntityValidator;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Log4j
public class ReactEAV {
    private ReactConstantConfiguration config;
    private ReactConnectionsDataBucket dataBucket;
    private ReacTask rootNode;
    private Set<ConditionExecutor> executors;
    private List<ReactQueryTaskHolder> reactQueryTaskHolders;
    private ReactQueryBuilder builder;
    private ReacQueryTasksPreparator preparator;

    @Autowired
    private ReactEntityValidator validator = new ReactEntityValidator();

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    ReactEAV(Class entityClass, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ReactConstantConfiguration config, ReactConnectionsDataBucket dataBucket, ReactQueryBuilder builder) {
        validator.isTargetClassAReactEntity(entityClass);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.config = config;
        this.dataBucket = dataBucket;
        this.builder = builder;
        this.rootNode = new ReacTask(null, this, entityClass, true, null, null, false, null);
        this.rootNode.setObjectClass(entityClass);
        this.executors = new HashSet<>();
    }

    ReactConnectionsDataBucket getDataBucket() {
        return dataBucket;
    }

    public ReacTask fetchRootChild(Class innerEntityClass) {
        checkInnerRelations(rootNode.getObjectClass(), dataBucket.getOuterRelationsMap().get(innerEntityClass).keySet());
        return fetchingOrderCreation(innerEntityClass, null);
    }

    public ReacTask fetchRootReference(Class innerEntityClass, String referenceName) {
        boolean innerHasIt = false;
        for (EntityReferenceRelationshipsData data : dataBucket.getEntityReferenceRelationsMap().get(innerEntityClass).values()) {
            if (data.getOuterClass().equals(rootNode.getObjectClass())) {
                innerHasIt = true;
            }
        }
        if (!innerHasIt) {
            throw new WrongFetchException(rootNode.getObjectClass(), innerEntityClass, "root");
        }
        return fetchingOrderCreation(innerEntityClass, referenceName);
    }

    private void checkInnerRelations(Class clazz, Set<Class> set) {
        boolean rootHasIt = false;
        for (Class currentClass : set) {
            if (currentClass.equals(clazz)) rootHasIt = true;
        }
        if (!rootHasIt) {
            throw new WrongFetchException(clazz, rootNode.getObjectClass(), "root");
        }
    }

    private ReacTask fetchingOrderCreation(Class innerEntityClass, String referenceId) {
        validator.isTargetClassAReactEntity(innerEntityClass);
        ReacTask childNode = new ReacTask(rootNode, this, innerEntityClass, false, null, null, false, referenceId);
        rootNode.addInnerObject(childNode);
        return childNode;
    }

    void generateCondition(ReactConditionData data) {
        addConditionExecutor(data);
    }

    public ReactEAV addCondition(ReactCondition condition, ConditionExecutionMoment moment) {
        ReactConditionData data = new ReactConditionData(condition, rootNode, moment);
        addConditionExecutor(data);
        return this;
    }

    private void addConditionExecutor(ReactConditionData data) {
        Class<ConditionExecutor> executorClass = data.getCondition().getNeededConditionExecutor();
        boolean executorAlreadyExists = false;
        for (ConditionExecutor existingExecutor : executors) {
            if (existingExecutor.getClass().equals(executorClass)) {
                existingExecutor.addReactConditionData(data);
                executorAlreadyExists = true;
            }
        }
        if (!executorAlreadyExists) {
            try {
                ConditionExecutor executor = executorClass.newInstance();
                executor.addReactConditionData(data);
                executors.add(executor);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e);
            }
        }
    }

    public Object getSingleEntityWithId(int targetId) throws ResultEntityNullException {
        rootNode.setForSingleObject(true);
        rootNode.setTargetId(targetId);
        rootNode.setAscend(false);
        rootNode.setOrderingParameter(null);
        reactQueryTaskHolders = reacTaskPreparator();
        Object result;
        try {
            result = launchProcess().get(0);
        } catch (NullPointerException e) {
            throw new ResultEntityNullException();
        }
        conditionExecution(ConditionExecutionMoment.AFTER_QUERY);
        return result;
    }

    public List getEntityCollection() throws ResultEntityNullException {
        rootNode.setForSingleObject(false);
        rootNode.setTargetId(null);
        rootNode.setAscend(false);
        rootNode.setOrderingParameter(null);
        reactQueryTaskHolders = reacTaskPreparator();
        List result;
        result = launchProcess();
        if (null == result) {
            throw new ResultEntityNullException();
        }
        conditionExecution(ConditionExecutionMoment.AFTER_QUERY);
        return result;
    }

    public List getEntityCollectionOrderByParameter(String orderingParameter, boolean ascend) throws ResultEntityNullException {
        rootNode.setForSingleObject(false);
        rootNode.setTargetId(null);
        rootNode.setAscend(ascend);
        rootNode.setOrderingParameter(orderingParameter);
        reactQueryTaskHolders = reacTaskPreparator();
        List result;
        result = launchProcess();
        if (null == result) {
            throw new ResultEntityNullException();
        }
        conditionExecution(ConditionExecutionMoment.AFTER_QUERY);
        return result;
    }

    private void conditionExecution(ConditionExecutionMoment moment) {
        if (!executors.isEmpty()) {
            for (ConditionExecutor executor : executors) {
                executor.executeAll(moment);
            }
        }
    }

    private List<ReactQueryTaskHolder> reacTaskPreparator() {
        List<ReactQueryTaskHolder> taskList = new ArrayList<>();
        creatingReacTaskTree(taskList, rootNode);
        return taskList;
    }

    private void creatingReacTaskTree(List<ReactQueryTaskHolder> taskList, ReacTask task) {
        addReferenceLinks(task);
        if (!task.getInnerObjects().isEmpty()) {
            for (ReacTask reacTask : task.getInnerObjects()) {
                creatingReacTaskTree(taskList, reacTask);
            }
        }
        taskList.add(prepareReacTask(task));
    }

    private ReactQueryTaskHolder prepareReacTask(ReacTask currentNode) {
        StringBuilder query = null;
        Map<String, Object> sqlParameterSource = new HashMap<>();
        if (currentNode.isForSingleObject()) {
            try {
                query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, true, null, false);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                log.error(e);
            }
            sqlParameterSource.put(config.getEntityTypeIdConstant(), currentNode.getThisClassObjectTypeName());
            sqlParameterSource.put(config.getEntityIdConstant(), currentNode.getTargetId());
            if (currentNode.hasReferencedObjects()) {
                for (EntityReferenceTaskData data : currentNode.getCurrentEntityReferenceTasks().values()) {
                    sqlParameterSource.put(data.getInnerClassObjectTypeName(), dataBucket.getClassesMap().get(data.getInnerClass()));
                }
            }
        } else {
            if (null != currentNode.getOrderingParameter()) {
                try {
                    query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, false, currentNode.getOrderingParameter(), currentNode.isAscend());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    log.error(e);
                }
                sqlParameterSource.put(config.getEntityTypeIdConstant(), currentNode.getThisClassObjectTypeName());
                if (currentNode.hasReferencedObjects()) {
                    for (EntityReferenceTaskData data : currentNode.getCurrentEntityReferenceTasks().values()) {
                        sqlParameterSource.put(data.getInnerClassObjectTypeName(), dataBucket.getClassesMap().get(data.getInnerClass()));
                    }
                }
            } else {
                try {
                    query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, false, null, false);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    log.error(e);
                }
                sqlParameterSource.put(config.getEntityTypeIdConstant(), currentNode.getThisClassObjectTypeName());
                if (currentNode.hasReferencedObjects()) {
                    for (EntityReferenceTaskData data : currentNode.getCurrentEntityReferenceTasks().values()) {
                        sqlParameterSource.put(data.getInnerClassObjectTypeName(), dataBucket.getClassesMap().get(data.getInnerClass()));
                    }
                }
            }
        }
        return new ReactQueryTaskHolder(currentNode, query, sqlParameterSource);
    }

    //Добавляем в задание ссылки и маркера на reference-объекты
    private void addReferenceLinks(ReacTask currentTask) {
        int per = 0;
        for (ReacTask task : currentTask.getInnerObjects()) {
            for (Map.Entry<String, EntityReferenceRelationshipsData> outerData : task.getCurrentEntityReferenceRelations().entrySet()) {
                if (outerData.getValue().getOuterClass().equals(currentTask.getObjectClass()) && null != task.getReferenceId() && task.getReferenceId().equals(outerData.getKey())) {
                    EntityReferenceTaskData newReferenceTask = new EntityReferenceTaskData(task.getObjectClass(), task.getObjectClass().getSimpleName(), outerData.getValue().getOuterFieldName(), outerData.getValue().getReferenceAttrId());
                    currentTask.addCurrentEntityReferenceTasks(per++, newReferenceTask);
                }
            }
        }
    }

    private List launchProcess() {
        ListIterator<ReactQueryTaskHolder> holderIterator = reactQueryTaskHolders.listIterator(reactQueryTaskHolders.size());
        while(holderIterator.hasPrevious()) {
            ReactQueryTaskHolder holder = holderIterator.previous();
            holder.getNode().manageParentAndReferenceLists();
            if (holder.getNode().isForSingleObject()) {
                Object result = null;
                try {
                    result = namedParameterJdbcTemplate.queryForObject(holder.getQuery().toString(), holder.getSource(), new ReactEntityRowMapper(holder.getNode(), config.getDateAppender()));
                    holder.getNode().getResultList().add(result);
                } catch (EmptyResultDataAccessException empty) {
                    StringBuilder errorBuilder = new StringBuilder();
                    errorBuilder.append("ReactEAV failed to get single object from database, DB returned null. This may be because there is no entity objects in database.");
                    errorBuilder.append("Root entity class=").append(rootNode.getObjectClass());
                    errorBuilder.append("Entity-that-returned-null-class=").append(holder.getNode().getObjectClass());
                    errorBuilder.append("Target entity id=").append(holder.getNode().getTargetId());
                    errorBuilder.append("Query=").append(holder.getQuery());
                    errorBuilder.append("isForSibleObject? :").append(holder.getNode().isForSingleObject());
                    log.log(Level.WARN, errorBuilder.toString(), empty);
                    if (holder.getNode().equals(rootNode)) {
                        return null;
                    }
                }
            } else {
                List result;
                    try {
                        if (null!=holder.getNode().getParentalIdsForChildFetch()){
                            builder.appendChildWhereClause(holder.getQuery(), holder.getNode().getParentalIdsForChildFetch());
                        }
                        if (null!=holder.getNode().getObjectIdsForReferenceFetch()){
                            builder.appendReferenceWhereClause(holder.getQuery(), holder.getNode().getObjectIdsForReferenceFetch());
                        }
                        result = (List) namedParameterJdbcTemplate.query(holder.getQuery().toString(), holder.getSource(), new RowMapperResultSetExtractor(new ReactEntityRowMapper(holder.getNode(), config.getDateAppender())));
                        holder.getNode().setResultList(result);
                    } catch (EmptyResultDataAccessException empty) {
                        StringBuilder errorBuilder = new StringBuilder();
                        errorBuilder.append("ReactEAV failed to get object collection from database, DB returned null. This may be because there is no entity objects in database.");
                        errorBuilder.append("Root entity class=").append(rootNode.getObjectClass());
                        errorBuilder.append("Entity-that-returned-null-class=").append(holder.getNode().getObjectClass());
                        errorBuilder.append("Query=").append(holder.getQuery());
                        errorBuilder.append("Sorting parameter=").append(holder.getNode().getOrderingParameter());
                        log.log(Level.WARN, errorBuilder.toString(), empty);
                        if (holder.getNode().equals(rootNode)) {
                            return null;
                        }
                    }
                }
            }

        if (reactQueryTaskHolders.size() == 1) {
            return reactQueryTaskHolders.get(0).getNode().getResultList();
        }
        ReacResultDataConnector dataConnector = new ReacResultDataConnector(rootNode);
        return dataConnector.connectEntitiesAndReturn();
    }

    //Метод создания ссылки в билдере
    private StringBuilder getQueryForEntity(LinkedHashMap<String, EntityVariablesData> currentNodeVariables, ReacTask currentNode, boolean isSearchById, String orderingParameter, boolean ascend) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        WhereAppendingConditionExecutor afterWhereExecutor = null;
        for (ConditionExecutor executor: executors){
            if (executor.getExecutorClass().equals(WhereAppendingConditionExecutor.class)){
                afterWhereExecutor = (WhereAppendingConditionExecutor) executor;
            }
        }
        return builder.getQueryForEntity(currentNodeVariables, currentNode, isSearchById, orderingParameter, ascend, afterWhereExecutor);
    }
}
