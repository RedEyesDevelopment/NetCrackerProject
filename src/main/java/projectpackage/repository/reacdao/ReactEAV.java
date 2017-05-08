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
       return fetchingOrderCreation(innerEntityClass, ReactResultQuantityType.SINGLE_OBJECT);
    }

    public FetchNode fetchInnerEntityCollectionClass(Class<? extends ReacEntity> innerEntityClass){
        return fetchingOrderCreation(innerEntityClass, ReactResultQuantityType.OBJECTS_LIST);
    }

    private FetchNode fetchingOrderCreation(Class<? extends ReacEntity> innerEntityClass, ReactResultQuantityType entityContainer){
        FetchNode childNode = new FetchNode(this,entityContainer);
        childNode.setObjectClass(innerEntityClass);
        rootNode.addFetchedNode(childNode);
        return childNode;
    }

    public ReacEntity getSingleEntity(){
        rootNode.setContainer(ReactResultQuantityType.SINGLE_OBJECT);
        String query=null;
        try {
            query = getQueryForEntity(rootNode);
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
        ReacEntity result = (ReacEntity) namedParameterJdbcTemplate.queryForObject(query, namedParameters, new ReactEntityRowMapper(rootNode.getObjectClass(),rootNode.getCurrentEntityParameters(),config.getDateAppender()));
        return result;
    }

    public ReacEntity getSingleEntityWithId(int id){
        rootNode.setContainer(ReactResultQuantityType.SINGLE_OBJECT);
        return null;
    }

    public List<? extends ReacEntity> getEntityCollection(){
        rootNode.setContainer(ReactResultQuantityType.OBJECTS_LIST);
        String query=null;
        try {
            query = getQueryForEntity(rootNode);
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

    public List<? extends ReacEntity> getEntityCollectionOrderByColumn(String orderingColumn) {
        rootNode.setContainer(ReactResultQuantityType.OBJECTS_LIST);
        rootNode.returnOrderedByColumn(orderingColumn);
        return null;
    }

    public List<? extends ReacEntity> getEntityCollectionOrderByParameter(String orderingParameter) {
        rootNode.setContainer(ReactResultQuantityType.OBJECTS_LIST);
        rootNode.returnOrderedByParameter(orderingParameter);
        return null;
    }

    public List<? extends ReacEntity> getEntityCollectionOrderByComparator(Comparator<? extends ReacEntity> entityComparator){
        rootNode.setContainer(ReactResultQuantityType.OBJECTS_LIST);
        return null;
    }

    //Метод создания ссылки в билдере
    private String getQueryForEntity(FetchNode currentNode) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        StringBuilder queryBuilder = new StringBuilder();
        ObjectTableNameGenerator attributesTablesNameGenerator = new ObjectTableNameGenerator(config.getAttributesPermanentTableName());
        LinkedHashMap<String, EntityVariablesNode> currentNodeVariables;
//        Кастуем класс
        Class currentNodeObjectClass = currentNode.getObjectClass();
        ReacEntity reacEntity = (ReacEntity) currentNodeObjectClass.newInstance();
        currentNode.setEntity(reacEntity);
        String currentEntityObjectType=reacEntity.getEntityObjectTypeForEav();
        //Получаем метод getEntityFields
        Method currentNodeObjectClassMethod = currentNodeObjectClass.getMethod("getEntityFields");
        currentNodeVariables = (LinkedHashMap<String, EntityVariablesNode>) currentNodeObjectClassMethod.invoke(reacEntity);
        currentNode.setCurrentEntityParameters(new LinkedHashMap<String, EntityVariablesNode>(currentNodeVariables));
        Iterator currentNodeVariablesMapIterator = currentNodeVariables.entrySet().iterator();
        LinkedHashMap<String, String> currentNodeAttributesMap = new LinkedHashMap<>();
        queryBuilder.append("SELECT");
        boolean firstKeyFlag=false;
        //Пробегаемся по мапе параметров, аппендим их и удаляем те, которые прямо в таблице OBJECTS находятся
        while (currentNodeVariablesMapIterator.hasNext()) {
                Map.Entry<String, EntityVariablesNode> objectPropertiesMapIteratorEntry = (Map.Entry) currentNodeVariablesMapIterator.next();
            String objectParameterKey = objectPropertiesMapIteratorEntry.getKey();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseColumnValue();
            if (!firstKeyFlag) {
                firstKeyFlag = true;
            } else queryBuilder.append(", ");
            if (databaseColumnValue.startsWith("%")){
                queryBuilder.append(" "+config.getRootTableName()+"."+databaseColumnValue.substring(1)+" \""+objectParameterKey+"\"");
                currentNodeVariablesMapIterator.remove();
            } else {
                queryBuilder.append(attributesTablesNameGenerator.getNextTableName()+"."+config.getV()+" \""+objectParameterKey+"\", ");
                queryBuilder.append(attributesTablesNameGenerator.getCurrentTableName()+"."+config.getDv()+" \""+objectParameterKey+config.getDateAppender()+"\"");
//                currentNodeAttributesMap.put(objectParameterKey,databaseColumnValue);
            }
        }
        queryBuilder.append("\nFROM "+config.getObjectsTableName()+ " "+config.getRootTableName());
        queryBuilder.append(", "+config.getObjTypesTableName()+" "+config.getRootTypesTableName());
        for (int i=1;i<=attributesTablesNameGenerator.getTablesCounter();i++){
            queryBuilder.append(", "+config.getAttributesTableName()+" "+config.getAttributesPermanentTableName()+i);
            queryBuilder.append(", "+config.getAttrTypesTableName()+" "+config.getAttrTypesPermanentTableName()+i);
        }
        queryBuilder.append("\nWHERE ");
        queryBuilder.append(config.getRootTypesTableName()+"."+config.getCd()+"=:"+config.getEntityTypeIdConstant());
        queryBuilder.append("\nAND "+config.getRootTableName()+"."+config.getOtid()+"="+config.getRootTypesTableName()+"."+config.getOtid());
//        currentNodeVariablesMapIterator = currentNodeAttributesMap.entrySet().iterator();
        currentNodeVariablesMapIterator = currentNodeVariables.entrySet().iterator();
        for (int i=1;i<=attributesTablesNameGenerator.getTablesCounter();i++){
            queryBuilder.append("\nAND "+config.getRootTableName()+"."+config.getOid()+"="+config.getAttributesPermanentTableName()+i+"."+config.getOid());
            queryBuilder.append("\nAND "+config.getRootTableName()+"."+config.getOtid()+"="+config.getAttrTypesPermanentTableName()+i+"."+config.getOtid());
            queryBuilder.append("\nAND "+config.getAttributesPermanentTableName()+i+"."+config.getAid()+"="+config.getAttrTypesPermanentTableName()+i+"."+config.getAid());
            Map.Entry<String, EntityVariablesNode> objectPropertiesMapIteratorEntry = (Map.Entry) currentNodeVariablesMapIterator.next();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseColumnValue();
            if (databaseColumnValue.startsWith("%")){
                queryBuilder.append("\nAND "+config.getAttrTypesPermanentTableName()+i+"."+config.getCd()+"='"+databaseColumnValue.substring(1)+"'");
            } else {
                queryBuilder.append("\nAND "+config.getAttrTypesPermanentTableName()+i+"."+config.getCd()+"='"+databaseColumnValue+"'");
            }
        }
//        queryBuilder.append(";");
        return queryBuilder.toString();
    }

}
