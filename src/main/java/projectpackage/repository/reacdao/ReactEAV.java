package projectpackage.repository.reacdao;

import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.support.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ReactEAV {
    ReactConstantConfiguration config;
    private FetchNode rootNode;

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    ReactEAV(Class<? extends ReacEntity> entityClass, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ReactConstantConfiguration config) {
            this.rootNode = new FetchNode(this);
            this.rootNode.setObjectClass(entityClass);
            this.namedParameterJdbcTemplate=namedParameterJdbcTemplate;
            this.config = config;
    }

    public FetchNode fetchInnerEntity(Class<? extends ReacEntity> innerEntityClass){
       return fetchingOrderCreation(innerEntityClass);
    }

    public FetchNode fetchInnerEntityCollectionClass(Class<? extends ReacEntity> innerEntityClass){
        return fetchingOrderCreation(innerEntityClass);
    }

    private FetchNode fetchingOrderCreation(Class<? extends ReacEntity> innerEntityClass){
        FetchNode childNode = new FetchNode(this);
        childNode.setObjectClass(innerEntityClass);
        rootNode.addFetchedNode(childNode);
        return childNode;
    }

    public ReacEntity getSingleEntityWithId(int targetId){
        String query=null;
        try {
            query = generateInformationForBuildingQuery(rootNode, true ,null, false);
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
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(config.getEntityTypeIdConstant(), rootNode.getEntity().getEntityObjectTypeForEav()).addValue(config.getEntityIdConstant(), targetId);

        ReacEntity result = (ReacEntity) namedParameterJdbcTemplate.queryForObject(query, namedParameters, new ReactEntityRowMapper(rootNode.getObjectClass(),rootNode.getCurrentEntityParameters(),config.getDateAppender()));
        return result;
    }


    public List<? extends ReacEntity> getEntityCollection(){
        String query=null;
        try {
            query = generateInformationForBuildingQuery(rootNode,false, null, false);
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
        SqlParameterSource namedParameters = new MapSqlParameterSource(config.getEntityTypeIdConstant(), rootNode.getEntity().getEntityObjectTypeForEav());
        List<ReacEntity> result = (List<ReacEntity>) namedParameterJdbcTemplate.query(query, namedParameters,  new RowMapperResultSetExtractor<ReacEntity>(new ReactEntityRowMapper(rootNode.getObjectClass(),rootNode.getCurrentEntityParameters(),config.getDateAppender())));
        return result;
    }

    public List<? extends ReacEntity> getEntityCollectionOrderByParameter(String orderingParameter, boolean ascend) {
        String query=null;
        try {
            query = generateInformationForBuildingQuery(rootNode,false, orderingParameter, ascend);
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
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(config.getEntityTypeIdConstant(), rootNode.getEntity().getEntityObjectTypeForEav());

        List<ReacEntity> result = (List<ReacEntity>) namedParameterJdbcTemplate.query(query, namedParameters,  new RowMapperResultSetExtractor<ReacEntity>(new ReactEntityRowMapper(rootNode.getObjectClass(),rootNode.getCurrentEntityParameters(),config.getDateAppender())));
        return result;
    }

    private String generateInformationForBuildingQuery(FetchNode currentNode, boolean isSearchById, String orderingParameter, boolean ascend) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        LinkedHashMap<String, EntityVariablesNode> currentNodeVariables;
//        Кастуем класс
        Class currentNodeObjectClass = currentNode.getObjectClass();
        ReacEntity reacEntity = (ReacEntity) currentNodeObjectClass.newInstance();
        currentNode.setEntity(reacEntity);
        //Получаем метод getEntityFields
        Method currentNodeObjectClassMethod = currentNodeObjectClass.getMethod("getEntityFields");
        currentNodeVariables = (LinkedHashMap<String, EntityVariablesNode>) currentNodeObjectClassMethod.invoke(reacEntity);
        currentNode.setCurrentEntityParameters(new LinkedHashMap<String, EntityVariablesNode>(currentNodeVariables));
        return getQueryForEntity(currentNodeVariables, currentNode, isSearchById, orderingParameter, ascend);
    }

    //Метод создания ссылки в билдере
    private String getQueryForEntity( LinkedHashMap<String, EntityVariablesNode> currentNodeVariables, FetchNode currentNode, boolean isSearchById, String orderingParameter, boolean ascend) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //Создаём стрингбилдер и ReactQueryBuilder, нацеленный на стрингбилдер.
        StringBuilder queryBuilder = new StringBuilder();
        ReactQueryBuilder builder = new ReactQueryBuilder(queryBuilder, config);

        //Создаём генератор имён для аттрибутов.
        ObjectTableNameGenerator attributesTablesNameGenerator = new ObjectTableNameGenerator(config.getAttributesPermanentTableName());
        Map<String, String> attributesNameMap = currentNode.getTableObjectsMap();

        // Создаём ссылку через билдер
        builder.appendSelectWord();

        //Пробегаемся по мапе параметров, аппендим их и удаляем те, которые прямо в таблице OBJECTS находятся
        Iterator currentNodeVariablesMapIterator = currentNodeVariables.entrySet().iterator();
        while (currentNodeVariablesMapIterator.hasNext()) {
                Map.Entry<String, EntityVariablesNode> objectPropertiesMapIteratorEntry = (Map.Entry) currentNodeVariablesMapIterator.next();
            String objectParameterKey = objectPropertiesMapIteratorEntry.getKey();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseColumnValue();

            if (databaseColumnValue.startsWith("%")){
                //Этот объект прямо из таблицы объектов, поэтому очищаем мапу от него.
                builder.appendSelectColumnWithNaming(config.getRootTableName(), databaseColumnValue.substring(1), objectParameterKey);
                currentNodeVariablesMapIterator.remove();
            } else {
                // Мы заранее не знаем где находятся данные - в VALUE или DATE_VALUE, поэтому выбираем и то и то.
                String nextAttributeName = attributesTablesNameGenerator.getNextTableName();
                attributesNameMap.put(objectParameterKey,nextAttributeName+".VALUE");
                builder.appendSelectColumnWithValueAndNaming(nextAttributeName, objectParameterKey);
                builder.appendSelectColumnWithDataValueAndNaming(nextAttributeName, objectParameterKey);
            }
        }

        //Начинаем аппендить раздел FROM
        builder.appendFromWord();
        builder.appendFromTableWithNaming(config.getObjectsTableName(), config.getRootTableName());
        builder.appendFromTableWithNaming(config.getObjTypesTableName(), config.getRootTypesTableName());

        //Аппендим таблицы ATTRIBUTES и ATTRTYPES, сколько есть аттрибутов, столько раз и аппендим, прибавляя к имени таблиц по единице
        for (int i=1;i<=attributesTablesNameGenerator.getTablesCounter();i++){
            builder.appendFromTableWithNaming(config.getAttributesTableName(), config.getAttributesPermanentTableName()+i);
            builder.appendFromTableWithNaming(config.getAttrTypesTableName(), config.getAttrTypesPermanentTableName()+i);
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
        for (int i=1;i<=attributesTablesNameGenerator.getTablesCounter();i++){
            // WHERE OBJECTS.OBJECT_ID=ATTRIBUTES.OBJECT_ID
            builder.appendWhereConditionWithTwoTablesEqualsByOB_ID(config.getRootTableName(), config.getAttributesPermanentTableName()+i);
            // WHERE OBJECTS.OBJECT_TYPE_ID=ATTRTYPES.OBJECT_TYPE_ID
            builder.appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(config.getRootTableName(), config.getAttrTypesPermanentTableName()+i);
            //WHERE ATTRIBUTES.ATTR_ID=ATTRTYPES.ATTR_ID
            builder.appendWhereConditionWithTwoTablesEqualsByAT_ID(config.getAttributesPermanentTableName()+i, config.getAttrTypesPermanentTableName()+i);

            Map.Entry<String, EntityVariablesNode> objectPropertiesMapIteratorEntry = (Map.Entry) currentNodeVariablesMapIterator.next();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseColumnValue();
            //WHERE ATTRTYPE.CODE=Код из таблицы кодов полйе объекта
            if (databaseColumnValue.startsWith("%")){
                builder.appendWhereConditionWithTableCodeEqualsToValue(config.getAttrTypesPermanentTableName()+i, databaseColumnValue.substring(1));
            } else {
                builder.appendWhereConditionWithTableCodeEqualsToValue(config.getAttrTypesPermanentTableName()+i, databaseColumnValue);
            }
        }
        //Добавляем кляузу WHERE OBJECTS.OBJECT_ID=...
        if (isSearchById) builder.appendWhereConditionWithRootTableObjectIdSearching();

        //Сортируем по колонке
        if (null!=orderingParameter) {
            builder.appendOrderBy(attributesNameMap.get(orderingParameter), ascend);
        } else builder.closeQueryBuilder();

        return queryBuilder.toString();
    }

}
