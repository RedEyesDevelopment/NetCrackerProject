package projectpackage.repository.reacdao;

import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import projectpackage.repository.reacdao.fetch.EntityVariablesNode;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.querying.ReactQueryBuilder;
import projectpackage.repository.reacdao.querying.ReactQueryHolder;
import projectpackage.repository.reacdao.support.ObjectTableNameGenerator;
import projectpackage.repository.reacdao.support.ReactConstantConfiguration;
import projectpackage.repository.reacdao.support.ReactEntityRowMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ReactEAV {
    ReactConstantConfiguration config;
    private ReacTask rootNode;
    private List<ReactQueryHolder> reactQueryHolders;

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    ReactEAV(Class<? extends ReacEntity> entityClass, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ReactConstantConfiguration config) {
        this.rootNode = new ReacTask(this, entityClass, true, null, null, false);
        this.rootNode.setObjectClass(entityClass);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.config = config;
    }

    public ReacTask fetchSingleInnerEntity(Class<? extends ReacEntity> innerEntityClass) {
        ReacTask newReacTask = fetchingOrderCreation(innerEntityClass, true, null, null, false);
        return newReacTask;
    }

    public ReacTask fetchInnerEntityCollection(Class<? extends ReacEntity> innerEntityClass) {
        ReacTask newReacTask = fetchingOrderCreation(innerEntityClass, false, null, null, false);
        return newReacTask;
    }

    public ReacTask fetchInnerEntityCollectionOrderBy(Class<? extends ReacEntity> innerEntityClass, String orderingParameter, boolean ascend) {
        ReacTask newReacTask = fetchingOrderCreation(innerEntityClass, false, null, orderingParameter, ascend);
        return newReacTask;
    }

    private ReacTask fetchingOrderCreation(Class<? extends ReacEntity> innerEntityClass, boolean forSingleObject, Integer targetId, String orderingParameter, boolean ascend) {
        ReacTask childNode = new ReacTask(this, innerEntityClass, forSingleObject, targetId, orderingParameter, ascend);
        childNode.setObjectClass(innerEntityClass);
        rootNode.addInnerObject(childNode);
        return childNode;
    }

    public ReacEntity getSingleEntityWithId(int targetId) {
        rootNode.setForSingleObject(true);
        rootNode.setTargetId(targetId);
        rootNode.setAscend(false);
        rootNode.setOrderingParameter(null);
        reactQueryHolders = reacTaskPreparator();
        launchProcess();
        return null;
    }


    public List<? extends ReacEntity> getEntityCollection() {
        rootNode.setForSingleObject(false);
        rootNode.setTargetId(null);
        rootNode.setAscend(false);
        rootNode.setOrderingParameter(null);
        reactQueryHolders = reacTaskPreparator();
        launchProcess();
        return null;
    }

    public List<? extends ReacEntity> getEntityCollectionOrderByParameter(String orderingParameter, boolean ascend) {
        rootNode.setForSingleObject(false);
        rootNode.setTargetId(null);
        rootNode.setAscend(ascend);
        rootNode.setOrderingParameter(orderingParameter);
        reactQueryHolders = reacTaskPreparator();
        launchProcess();
        return null;
    }

    private List<ReactQueryHolder> reacTaskPreparator() {
        List<ReactQueryHolder> taskList = new ArrayList<>();
        creatingReacTaskTree(taskList, rootNode);
        return taskList;
    }

    private void creatingReacTaskTree(List<ReactQueryHolder> taskList, ReacTask task) {
        if (!task.getInnerObjects().isEmpty()) {
            for (ReacTask reacTask : task.getInnerObjects()) {
                creatingReacTaskTree(taskList, reacTask);
            }
        }
        taskList.add(prepareReacTask(task));
    }


    private ReactQueryHolder prepareReacTask(ReacTask currentNode) {
        String query = null;
        SqlParameterSource sqlParameterSource = null;
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
                sqlParameterSource = new MapSqlParameterSource().addValue(config.getEntityTypeIdConstant(), currentNode.getEntity().getEntityObjectTypeForEav()).addValue(config.getEntityIdConstant(), currentNode.getTargetId());

        } else {
            if (null != currentNode.getOrderingParameter()) {
                try {
                    query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, false, currentNode.getOrderingParameter(), currentNode.isAscend());
                    System.out.println(query);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                    sqlParameterSource = new MapSqlParameterSource().addValue(config.getEntityTypeIdConstant(), currentNode.getEntity().getEntityObjectTypeForEav());
            } else {
                try {
                    query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, false, null, false);
                    System.out.println(query);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                    sqlParameterSource = new MapSqlParameterSource(config.getEntityTypeIdConstant(), currentNode.getEntity().getEntityObjectTypeForEav());
            }
        }
        return new ReactQueryHolder(currentNode, query, sqlParameterSource);
    }


    private void launchProcess() {
        for (ReactQueryHolder holder : reactQueryHolders) {
            if (holder.getNode().isForSingleObject()) {
                ReacEntity result = (ReacEntity) namedParameterJdbcTemplate.queryForObject(holder.getQuery(), holder.getSource(), new ReactEntityRowMapper(holder.getNode().getObjectClass(), holder.getNode().getCurrentEntityParameters(), config.getDateAppender()));
                holder.getNode().getResultList().add(result);
            } else {
                List<ReacEntity> result = (List<ReacEntity>) namedParameterJdbcTemplate.query(holder.getQuery(), holder.getSource(), new RowMapperResultSetExtractor<ReacEntity>(new ReactEntityRowMapper(holder.getNode().getObjectClass(), holder.getNode().getCurrentEntityParameters(), config.getDateAppender())));
                holder.getNode().setResultList(result);
            }
        }
        System.out.println("HIFOLKS");
    }


    //Метод создания ссылки в билдере
    private String getQueryForEntity(LinkedHashMap<String, EntityVariablesNode> currentNodeVariables, ReacTask currentNode, boolean isSearchById, String orderingParameter, boolean ascend) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //Создаём стрингбилдер и ReactQueryBuilder, нацеленный на стрингбилдер.
        StringBuilder queryBuilder = new StringBuilder();
        ReactQueryBuilder builder = new ReactQueryBuilder(queryBuilder, config);

        //Создаём генератор имён для аттрибутов.
        ObjectTableNameGenerator attributesTablesNameGenerator = new ObjectTableNameGenerator(config.getAttributesPermanentTableName());
        Map<String, String> attributesNameMap = new HashMap<>();

        // Создаём ссылку через билдер
        builder.appendSelectWord();

        //Пробегаемся по мапе параметров, аппендим их и удаляем те, которые прямо в таблице OBJECTS находятся
        Iterator currentNodeVariablesMapIterator = currentNodeVariables.entrySet().iterator();
        while (currentNodeVariablesMapIterator.hasNext()) {
            Map.Entry<String, EntityVariablesNode> objectPropertiesMapIteratorEntry = (Map.Entry) currentNodeVariablesMapIterator.next();
            String objectParameterKey = objectPropertiesMapIteratorEntry.getKey();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseAttrtypeCodeValue();

            if (databaseColumnValue.startsWith("%")) {
                //Этот объект прямо из таблицы объектов, поэтому очищаем мапу от него.
                builder.appendSelectColumnWithNaming(config.getRootTableName(), databaseColumnValue.substring(1), objectParameterKey);
                currentNodeVariablesMapIterator.remove();
            } else {
                // Мы заранее не знаем где находятся данные - в VALUE или DATE_VALUE, поэтому выбираем и то и то.
                String nextAttributeName = attributesTablesNameGenerator.getNextTableName();
                attributesNameMap.put(objectParameterKey, nextAttributeName + ".VALUE");
                builder.appendSelectColumnWithValueAndNaming(nextAttributeName, objectParameterKey);
                builder.appendSelectColumnWithDataValueAndNaming(nextAttributeName, objectParameterKey);
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

        //Аппендим раздел WHERE
        //OB_TY_ID = OBJECT_TYPE_ID
        //OB_ID = OBJECT_ID
        //AT_ID = ATTR_ID
        builder.appendWhereWord();
        builder.appendWhereConditionWithTableCodeEqualsToConstant(config.getRootTypesTableName(), config.getEntityTypeIdConstant());
        builder.appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(config.getRootTableName(), config.getRootTypesTableName());

        //Аппендим аттрибуты
        currentNodeVariablesMapIterator = currentNodeVariables.entrySet().iterator();
        for (int i = 1; i <= attributesTablesNameGenerator.getTablesCounter(); i++) {
            // WHERE OBJECTS.OBJECT_ID=ATTRIBUTES.OBJECT_ID
            builder.appendWhereConditionWithTwoTablesEqualsByOB_ID(config.getRootTableName(), config.getAttributesPermanentTableName() + i);
            // WHERE OBJECTS.OBJECT_TYPE_ID=ATTRTYPES.OBJECT_TYPE_ID
            builder.appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(config.getRootTableName(), config.getAttrTypesPermanentTableName() + i);
            //WHERE ATTRIBUTES.ATTR_ID=ATTRTYPES.ATTR_ID
            builder.appendWhereConditionWithTwoTablesEqualsByAT_ID(config.getAttributesPermanentTableName() + i, config.getAttrTypesPermanentTableName() + i);

            Map.Entry<String, EntityVariablesNode> objectPropertiesMapIteratorEntry = (Map.Entry) currentNodeVariablesMapIterator.next();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseAttrtypeCodeValue();
            //WHERE ATTRTYPE.CODE=Код из таблицы кодов полйе объекта
            if (databaseColumnValue.startsWith("%")) {
                builder.appendWhereConditionWithTableCodeEqualsToValue(config.getAttrTypesPermanentTableName() + i, databaseColumnValue.substring(1));
            } else {
                builder.appendWhereConditionWithTableCodeEqualsToValue(config.getAttrTypesPermanentTableName() + i, databaseColumnValue);
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
