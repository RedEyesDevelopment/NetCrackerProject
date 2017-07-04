package projectpackage.repository.reacteav;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.ConditionExecutor;
import projectpackage.repository.reacteav.conditions.ReactCondition;
import projectpackage.repository.reacteav.conditions.ReactConditionData;
import projectpackage.repository.reacteav.exceptions.WrongFetchException;
import projectpackage.repository.reacteav.querying.ReactQueryTaskHolder;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceRelationshipsData;
import projectpackage.repository.reacteav.support.ReactConnectionsDataBucket;
import projectpackage.repository.reacteav.support.ReactConstantConfiguration;
import projectpackage.repository.reacteav.support.ReactEntityValidator;

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
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private ReactEntityValidator validator;

    ReactEAV(Class entityClass, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ReactConstantConfiguration config, ReactConnectionsDataBucket dataBucket, ReactQueryBuilder builder, ReacQueryTasksPreparator preparator, ReactEntityValidator validator) {
        validator.isTargetClassAReactEntity(entityClass);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.config = config;
        this.dataBucket = dataBucket;
        this.builder = builder;
        this.preparator = preparator;
        this.validator = validator;
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
            if (currentClass.equals(clazz)) {
                rootHasIt = true;
            }
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

    public Object getSingleEntityWithId(int targetId) {
        rootNode.setForSingleObject(true);
        rootNode.setTargetId(targetId);
        rootNode.setAscend(false);
        rootNode.setOrderingParameter(null);
        reactQueryTaskHolders = preparator.prepareTasks(rootNode,executors,builder);
        Object result;
        try {
            result = launchProcess().get(0);
        } catch (NullPointerException e) {
            return null;
        }
        conditionExecution(ConditionExecutionMoment.AFTER_QUERY);
        return result;
    }

    public List getEntityCollection() {
        rootNode.setForSingleObject(false);
        rootNode.setTargetId(null);
        rootNode.setAscend(false);
        rootNode.setOrderingParameter(null);
        reactQueryTaskHolders = preparator.prepareTasks(rootNode,executors,builder);
        List result;
        result = launchProcess();
        if (null == result) {
            return Collections.emptyList();
        }
        conditionExecution(ConditionExecutionMoment.AFTER_QUERY);
        return result;
    }

    public List getEntityCollectionOrderByParameter(String orderingParameter, boolean ascend) {
        rootNode.setForSingleObject(false);
        rootNode.setTargetId(null);
        rootNode.setAscend(ascend);
        rootNode.setOrderingParameter(orderingParameter);
        reactQueryTaskHolders = preparator.prepareTasks(rootNode,executors,builder);
        List result;
        result = launchProcess();
        if (null == result) {
            return Collections.emptyList();
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
                    errorBuilder.append(config.getNewLineChar());
                    errorBuilder.append("Root entity class=").append(rootNode.getObjectClass());
                    errorBuilder.append(config.getNewLineChar());
                    errorBuilder.append("Entity-that-returned-null-class=").append(holder.getNode().getObjectClass());
                    errorBuilder.append(config.getNewLineChar());
                    errorBuilder.append("Target entity id=").append(holder.getNode().getTargetId());
                    errorBuilder.append(config.getNewLineChar());
                    errorBuilder.append("Query=").append(holder.getQuery());
                    errorBuilder.append(config.getNewLineChar());
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
                        errorBuilder.append(config.getNewLineChar());
                        errorBuilder.append("Root entity class=").append(rootNode.getObjectClass());
                        errorBuilder.append(config.getNewLineChar());
                        errorBuilder.append("Entity-that-returned-null-class=").append(holder.getNode().getObjectClass());
                        errorBuilder.append(config.getNewLineChar());
                        errorBuilder.append("Query=").append(holder.getQuery());
                        errorBuilder.append(config.getNewLineChar());
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


}
