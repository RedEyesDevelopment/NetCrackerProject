package projectpackage.repository.reacteav;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.PriceEqualsToRoomCondition;
import projectpackage.repository.reacteav.conditions.ReactCondition;
import projectpackage.repository.reacteav.conditions.ReactConditionData;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.reacteav.querying.ReactQueryBuilder;
import projectpackage.repository.reacteav.querying.ReactQueryTaskHolder;
import projectpackage.repository.reacteav.relationsdata.EntityAttrIdType;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceTaskData;
import projectpackage.repository.reacteav.relationsdata.EntityVariablesData;
import projectpackage.repository.reacteav.support.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Log4j
public class ReactEAV {
    private ReactConstantConfiguration config;
    private ReactConnectionsDataBucket dataBucket;
    private ReacTask rootNode;
    private List<ReactConditionData> conditions;
    private List<ReactQueryTaskHolder> reactQueryTaskHolders;

    @Autowired
    private ReactEntityValidator validator = new ReactEntityValidator();

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    ReactEAV(Class entityClass, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ReactConstantConfiguration config, ReactConnectionsDataBucket dataBucket) {
        validator.isTargetClassAReactEntity(entityClass);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.config = config;
        this.dataBucket = dataBucket;
        this.rootNode = new ReacTask(null, this, entityClass, true, null, null, false, null);
        this.rootNode.setObjectClass(entityClass);
        this.conditions = new ArrayList<>();
    }

    ReactConnectionsDataBucket getDataBucket() {
        return dataBucket;
    }

    public ReacTask fetchChildEntityCollection(Class innerEntityClass) {
        ReacTask newReacTask = fetchingOrderCreation(innerEntityClass, false, null, null, false, null);
        return newReacTask;
    }

    public ReacTask fetchReferenceEntityCollection(Class innerEntityClass, String referenceName) {
        ReacTask newReacTask = fetchingOrderCreation(innerEntityClass, false, null, null, false, referenceName);
        return newReacTask;
    }


    private ReacTask fetchingOrderCreation(Class innerEntityClass, boolean forSingleObject, Integer targetId, String orderingParameter, boolean ascend, String referenceId) {
        validator.isTargetClassAReactEntity(innerEntityClass);
        ReacTask childNode = new ReacTask(rootNode, this, innerEntityClass, forSingleObject, targetId, orderingParameter, ascend, referenceId);
        rootNode.addInnerObject(childNode);
        return childNode;
    }

    public ReactEAV addCondition(Class<? extends ReactCondition> conditionClass) {
        ReactCondition condition = null;
        try {
            condition = conditionClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ReactConditionData data = null;
        ConditionExecutionMoment moment = null;
        if (conditionClass.equals(PriceEqualsToRoomCondition.class)) {
            moment = ConditionExecutionMoment.AFTER_QUERY;
        }
        data = new ReactConditionData(condition, moment);
        this.conditions.add(data);
        return this;
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
        if (null != conditions && !conditions.isEmpty()) {
            conditionExecution(ConditionExecutionMoment.AFTER_QUERY);
        }
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
        if (null != conditions && !conditions.isEmpty()) {
            conditionExecution(ConditionExecutionMoment.AFTER_QUERY);
        }
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
        if (null != conditions && !conditions.isEmpty()) {
            conditionExecution(ConditionExecutionMoment.AFTER_QUERY);
        }
        return result;
    }

    private void conditionExecution(ConditionExecutionMoment moment) {
        for (ReactConditionData data : conditions) {
            ReacTask searcheable;
            if (!data.getCondition().getTargetClass().equals(rootNode.getObjectClass())) {
                searcheable = recursionConditionTaskSearching(rootNode, data.getClass());
            } else {
                searcheable = rootNode;
            }
            if (data.getMoment().equals(moment)) {
                ReactCondition condition = data.getCondition();
                condition.loadDataToParse(searcheable.getResultList());
                condition.execute();
            }
        }
    }

    private ReacTask recursionConditionTaskSearching(ReacTask currentTask, Class searcheableClass) {
        if (currentTask.getObjectClass().equals(searcheableClass)) {
            return currentTask;
        } else {
            for (ReacTask task : currentTask.getInnerObjects()) {
                return recursionConditionTaskSearching(task, searcheableClass);
            }
        }
        return null;
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
        String query = null;
        Map<String, Object> sqlParameterSource = new HashMap<>();
        if (currentNode.isForSingleObject()) {
            try {
                query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, true, null, false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            sqlParameterSource.put(config.getEntityTypeIdConstant(), currentNode.getThisClassObjectTypeName());
            sqlParameterSource.put(config.getEntityIdConstant(), currentNode.getTargetId());
            if (currentNode.hasReferencedObjects()) {
                for (EntityReferenceTaskData data : currentNode.getCurrentEntityReferenceTasks().values()) {
                    sqlParameterSource.put(data.getInnerClassObjectTypeName(), data.getInnerClassObjectTypeName());
                }
            }
        } else {
            if (null != currentNode.getOrderingParameter()) {
                try {
                    query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, false, currentNode.getOrderingParameter(), currentNode.isAscend());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                sqlParameterSource.put(config.getEntityTypeIdConstant(), currentNode.getThisClassObjectTypeName());
                if (currentNode.hasReferencedObjects()) {
                    for (EntityReferenceTaskData data : currentNode.getCurrentEntityReferenceTasks().values()) {
                        sqlParameterSource.put(data.getInnerClassObjectTypeName(), data.getInnerClassObjectTypeName());
                    }
                }
            } else {
                try {
                    query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, false, null, false);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                sqlParameterSource.put(config.getEntityTypeIdConstant(), currentNode.getThisClassObjectTypeName());
                if (currentNode.hasReferencedObjects()) {
                    for (EntityReferenceTaskData data : currentNode.getCurrentEntityReferenceTasks().values()) {
                        sqlParameterSource.put(data.getInnerClassObjectTypeName(), data.getInnerClassObjectTypeName());
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
                    EntityReferenceTaskData newReferenceTask = new EntityReferenceTaskData(currentTask.getObjectClass(), task.getObjectClass(), task.getThisClassObjectTypeName(), outerData.getValue().getOuterFieldName(), outerData.getValue().getInnerIdKey(), outerData.getValue().getOuterIdKey(), outerData.getValue().getReferenceAttrId());
                    currentTask.addCurrentEntityReferenceTasks(per++, newReferenceTask);
                }
            }
        }
    }

    private List launchProcess() {
        for (ReactQueryTaskHolder holder : reactQueryTaskHolders) {
            if (holder.getNode().isForSingleObject()) {
                Object result = null;
                try {
                    result = namedParameterJdbcTemplate.queryForObject(holder.getQuery(), holder.getSource(), new ReactEntityRowMapper(holder.getNode().getObjectClass(), holder.getNode().getCurrentEntityParameters(), holder.getNode().getCurrentEntityReferenceTasks(), holder.getNode().getReferenceIdRelations(), config.getDateAppender()));
                    holder.getNode().getResultList().add(result);
                } catch (EmptyResultDataAccessException empty) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("ReactEAV failed to get single object from database, DB returned null. This may be because there is no entity objects in database.");
                    sb.append("Root entity class=" + rootNode.getObjectClass());
                    sb.append("Entity-that-returned-null-class=" + holder.getNode().getObjectClass());
                    sb.append("Target entity id=" + holder.getNode().getTargetId());
                    sb.append("Query=" + holder.getQuery());
                    sb.append("isForSibleObject? :" + holder.getNode().isForSingleObject());
                    log.log(Level.WARN, sb.toString(), empty);
                    if (holder.getNode().equals(rootNode)) {
                        return null;
                    }
                }
            } else {
                List result = null;
                boolean cloned = false;
                for (ReactQueryTaskHolder currentHolder : reactQueryTaskHolders) {
                    if (!currentHolder.getNode().getResultList().isEmpty() && holder.getNode().getObjectClass().equals(currentHolder.getNode().getObjectClass())) {
                        result = new ArrayList(currentHolder.getNode().getResultList());
                        cloned = true;
                    }
                }
                if (!cloned) {
                    try {
                        result = (List) namedParameterJdbcTemplate.query(holder.getQuery(), holder.getSource(), new RowMapperResultSetExtractor(new ReactEntityRowMapper(holder.getNode().getObjectClass(), holder.getNode().getCurrentEntityParameters(), holder.getNode().getCurrentEntityReferenceTasks(), holder.getNode().getReferenceIdRelations(), config.getDateAppender())));
                        holder.getNode().setResultList(result);
                    } catch (EmptyResultDataAccessException empty) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("ReactEAV failed to get object collection from database, DB returned null. This may be because there is no entity objects in database.");
                        sb.append("Root entity class=" + rootNode.getObjectClass());
                        sb.append("Entity-that-returned-null-class=" + holder.getNode().getObjectClass());
                        sb.append("Query=" + holder.getQuery());
                        sb.append("Sorting parameter=" + holder.getNode().getOrderingParameter());
                        log.log(Level.WARN, sb.toString(), empty);
                        if (holder.getNode().equals(rootNode)) {
                            return null;
                        }
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
    private String getQueryForEntity(LinkedHashMap<String, EntityVariablesData> currentNodeVariables, ReacTask currentNode, boolean isSearchById, String orderingParameter, boolean ascend) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //Создаём стрингбилдер и ReactQueryBuilder, нацеленный на стрингбилдер.
        StringBuilder queryBuilder = new StringBuilder();
        ReactQueryBuilder builder = new ReactQueryBuilder(queryBuilder, config);

        //Создаём генератор имён для аттрибутов.
        ObjectTableNameGenerator attributesTablesNameGenerator = new ObjectTableNameGenerator(config.getAttributesPermanentTableName());

        //Создаём объекты для связей типа reference
        HashMap<String, EntityAttrIdType> objReferenceConnections = new HashMap<>();
        ObjectTableNameGenerator referenceNameGenerator = null;
        if (currentNode.hasReferencedObjects()) {
            referenceNameGenerator = new ObjectTableNameGenerator(config.getReferenceTypePermanentTableName());
        }

        //В этой мапе мы будем хранить значения (поле объекта-название в таблице вместе с именем таблицы) для последующей сортировки ORDER BY
        Map<String, String> attributesNameMap = new HashMap<>();

        // Создаём ссылку через билдер
        builder.appendSelectWord();

        //Пробегаемся по мапе параметров, аппендим их и удаляем те, которые прямо в таблице OBJECTS находятся
        LinkedHashMap<String, EntityVariablesData> permanentCurrentNodeVariables = new LinkedHashMap<>(currentNodeVariables);
        Iterator currentNodeVariablesMapIterator = permanentCurrentNodeVariables.entrySet().iterator();
        while (currentNodeVariablesMapIterator.hasNext()) {
            Map.Entry<String, EntityVariablesData> objectPropertiesMapIteratorEntry = (Map.Entry) currentNodeVariablesMapIterator.next();
            String objectParameterKey = objectPropertiesMapIteratorEntry.getKey();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseAttrtypeCodeValue();

            if (databaseColumnValue.startsWith("%")) {
                //Этот объект прямо из таблицы объектов, поэтому очищаем мапу от него.
                builder.appendSelectColumnWithNaming(config.getRootTableName(), databaseColumnValue.substring(1), objectParameterKey);
                attributesNameMap.put(objectParameterKey, config.getRootTableName() + "." + databaseColumnValue.substring(1));
                currentNodeVariablesMapIterator.remove();
            } else {
                // Мы заранее не знаем где находятся данные - в VALUE или DATE_VALUE, поэтому выбираем и то и то.
                String nextAttributeName = attributesTablesNameGenerator.getNextTableName();
                attributesNameMap.put(objectParameterKey, nextAttributeName + ".VALUE");
                builder.appendSelectColumnWithValueAndNaming(nextAttributeName, objectParameterKey);
                builder.appendSelectColumnWithDataValueAndNaming(nextAttributeName, objectParameterKey);
            }
        }

        //добавляем в selec-кляузу OBJREFERENCE.REFERENCE(т.е. айди объекта который вставляется в референс)

        if (currentNode.hasReferencedObjects()) {
            int i = 1;
            for (EntityReferenceTaskData reference : currentNode.getCurrentEntityReferenceTasks().values()) {
                String nextReferenceName = referenceNameGenerator.getNextTableName();
                String targetName = "R_" + nextReferenceName;
                builder.appendSelectColumnWithNaming(targetName, config.getOref(), targetName);
                EntityAttrIdType type = new EntityAttrIdType(reference.getInnerClass().getName(), reference.getReferenceAttrId());
                objReferenceConnections.put(targetName, type);
                i++;
            }
        }

        //Начинаем аппендить раздел FROM
        builder.appendFromWord();
        builder.appendFromTableWithNaming(config.getObjectsTableName(), config.getRootTableName());
        builder.appendFromTableWithNaming(config.getObjTypesTableName(), config.getRootTypesTableName());

        //Аппендим таблицы ATTRIBUTES и ATTRTYPES, сколько есть аттрибутов, столько раз и аппендим, прибавляя к имени таблиц по единице
        for (int i = 1; i <= attributesTablesNameGenerator.getTablesCounter(); i++) {
            builder.appendFromTableWithNaming(config.getAttributesTableName(), config.getAttributesPermanentTableName() + i);
            builder.appendFromTableWithNaming(config.getAttrTypesTableName(), config.getAttrTypesPermanentTableName() + i);
        }

        if (currentNode.hasReferencedObjects()) {
            for (int i = 1; i <= referenceNameGenerator.getTablesCounter(); i++) {
                builder.appendFromTableWithNaming(config.getAttrTypesTableName(), "AT_" + config.getReferenceTypePermanentTableName() + i);
                builder.appendFromTableWithNaming(config.getObjTypesTableName(), "OT_" + config.getReferenceTypePermanentTableName() + i);
                builder.appendFromTableWithNaming(config.getObjRefsTableName(), "R_" + config.getReferenceTypePermanentTableName() + i);
            }
        }

        //Аппендим раздел WHERE
        //OB_TY_ID = OBJECT_TYPE_ID
        //OB_ID = OBJECT_ID
        //AT_ID = ATTR_ID
        builder.appendWhereWord();
        builder.appendWhereConditionWithTableCodeEqualsToConstant(config.getRootTypesTableName(), config.getEntityTypeIdConstant());
        builder.appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(config.getRootTableName(), config.getRootTypesTableName());

        //Аппендим аттрибуты
        currentNodeVariablesMapIterator = permanentCurrentNodeVariables.entrySet().iterator();
        for (int i = 1; i <= attributesTablesNameGenerator.getTablesCounter(); i++) {
            // WHERE OBJECTS.OBJECT_ID=ATTRIBUTES.OBJECT_ID
            builder.appendWhereConditionWithTwoTablesEqualsByOB_ID(config.getRootTableName(), config.getAttributesPermanentTableName() + i);
            // WHERE OBJECTS.OBJECT_TYPE_ID=ATTRTYPES.OBJECT_TYPE_ID
            builder.appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(config.getRootTableName(), config.getAttrTypesPermanentTableName() + i);
            //WHERE ATTRIBUTES.ATTR_ID=ATTRTYPES.ATTR_ID
            builder.appendWhereConditionWithTwoTablesEqualsByAT_ID(config.getAttributesPermanentTableName() + i, config.getAttrTypesPermanentTableName() + i);

            Map.Entry<String, EntityVariablesData> objectPropertiesMapIteratorEntry = (Map.Entry) currentNodeVariablesMapIterator.next();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseAttrtypeCodeValue();
            //WHERE ATTRTYPE.CODE=Код из таблицы кодов полйе объекта
            if (databaseColumnValue.startsWith("%")) {
                builder.appendWhereConditionWithTableCodeEqualsToValue(config.getAttrTypesPermanentTableName() + i, databaseColumnValue.substring(1));
            } else {
                builder.appendWhereConditionWithTableCodeEqualsToValue(config.getAttrTypesPermanentTableName() + i, databaseColumnValue);
            }
        }

        //Аппендим reference
        if (currentNode.hasReferencedObjects()) {
            List<EntityAttrIdType> objTypelist = new LinkedList<>();
            for (EntityReferenceTaskData reference : currentNode.getCurrentEntityReferenceTasks().values()) {
                EntityAttrIdType type = new EntityAttrIdType(reference.getInnerClassObjectTypeName(), reference.getReferenceAttrId());
                objTypelist.add(type);
            }
            int j = 0;
            for (int i = 1; i <= referenceNameGenerator.getTablesCounter(); i++) {
                String attrTypeName = "AT_" + config.getReferenceTypePermanentTableName() + i;
                String objTypeName = "OT_" + config.getReferenceTypePermanentTableName() + i;
                String objReferenceName = "R_" + config.getReferenceTypePermanentTableName() + i;
                builder.appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(config.getRootTableName(), attrTypeName);
                builder.appendWhereConditionWithAttrTypeRefEqualsOB_TY_ID(attrTypeName, objTypeName);
                builder.appendWhereConditionWithTwoTablesEqualsByAT_ID(attrTypeName, objReferenceName);
                builder.appendWhereConditionWithTableCodeEqualsToConstant(objTypeName, objTypelist.get(j).getInnerClassObjectTypeName());
                if (null != objTypelist.get(j).getAttrId()) {
                    builder.appendWhereConditionWithObRefAttrIdEqualsInteger(attrTypeName, objTypelist.get(j).getAttrId());
                }
                j++;
                builder.appendWhereConditionWithTwoTablesEqualsByOB_ID(config.getRootTableName(), objReferenceName);
            }
        }

        for (EntityReferenceTaskData taskData : currentNode.getCurrentEntityReferenceTasks().values()) {
            Iterator iterator = objReferenceConnections.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, EntityAttrIdType> tgtReference = (Map.Entry<String, EntityAttrIdType>) iterator.next();
                String referenceInnerClassObjectTypeName = tgtReference.getValue().getInnerClassObjectTypeName();
                String taskDataInnerClassName = taskData.getInnerClass().getName();
                Integer referenceAttrId = tgtReference.getValue().getAttrId();
                Integer taskDataAttrId = taskData.getReferenceAttrId();
                if (referenceInnerClassObjectTypeName.equals(taskDataInnerClassName)) {
                    if (null != referenceAttrId && null != taskDataAttrId) {
                        if (referenceAttrId.equals(taskDataAttrId)) {
                            taskData.setInnerIdParameterNameForQueryParametersMap(tgtReference.getKey());
                        }
                    } else {
                        taskData.setInnerIdParameterNameForQueryParametersMap(tgtReference.getKey());
                    }
                }
            }
        }

        //Добавляем кляузу WHERE OBJECTS.OBJECT_ID=...
        if (isSearchById) builder.appendWhereConditionWithRootTableObjectIdSearching();

        //Сортируем по колонке
        if (null != orderingParameter) {
            builder.appendOrderBy(attributesNameMap.get(orderingParameter), ascend);
        } else builder.closeQueryBuilder();

        return queryBuilder.toString();
    }
}
