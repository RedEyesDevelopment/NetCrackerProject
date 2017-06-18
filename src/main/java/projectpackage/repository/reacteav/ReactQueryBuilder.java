package projectpackage.repository.reacteav;

import projectpackage.repository.reacteav.querying.ReactQueryAppender;
import projectpackage.repository.reacteav.relationsdata.EntityAttrIdType;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceTaskData;
import projectpackage.repository.reacteav.relationsdata.EntityVariablesData;
import projectpackage.repository.reacteav.support.ObjectTableNameGenerator;
import projectpackage.repository.reacteav.support.ReactConstantConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ReactQueryBuilder {
    ReactConstantConfiguration config;

    public ReactQueryBuilder(ReactConstantConfiguration config) {
        this.config = config;
    }

    String getQueryForEntity(LinkedHashMap<String, EntityVariablesData> currentNodeVariables, ReacTask currentNode, boolean isSearchById, String orderingParameter, boolean ascend) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //Создаём стрингбилдер и ReactQueryAppender, нацеленный на стрингбилдер.
        StringBuilder queryBuilder = new StringBuilder();
        ReactQueryAppender queryAppender = new ReactQueryAppender(queryBuilder, config);

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
        queryAppender.appendSelectWord();

        //Пробегаемся по мапе параметров, аппендим их и удаляем те, которые прямо в таблице OBJECTS находятся
        LinkedHashMap<String, EntityVariablesData> permanentCurrentNodeVariables = new LinkedHashMap<>(currentNodeVariables);
        Iterator currentNodeVariablesMapIterator = permanentCurrentNodeVariables.entrySet().iterator();
        while (currentNodeVariablesMapIterator.hasNext()) {
            Map.Entry<String, EntityVariablesData> objectPropertiesMapIteratorEntry = (Map.Entry) currentNodeVariablesMapIterator.next();
            String objectParameterKey = objectPropertiesMapIteratorEntry.getKey();
            String databaseNativeCodeValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseNativeCodeValue();

            if (null!=databaseNativeCodeValue) {
                    //Этот объект прямо из таблицы объектов, поэтому очищаем мапу от него.
                    queryAppender.appendSelectColumnWithNaming(config.getRootTableName(), databaseNativeCodeValue.substring(1), objectParameterKey);
                    attributesNameMap.put(objectParameterKey, config.getRootTableName() + "." + databaseNativeCodeValue.substring(1));
                    currentNodeVariablesMapIterator.remove();
            } else {
                // Мы заранее не знаем где находятся данные - в VALUE или DATE_VALUE, поэтому выбираем и то и то.
                String nextAttributeName = attributesTablesNameGenerator.getNextTableName();
                attributesNameMap.put(objectParameterKey, nextAttributeName + ".VALUE");
                queryAppender.appendSelectColumnWithValueAndNaming(nextAttributeName, objectParameterKey);
                queryAppender.appendSelectColumnWithDataValueAndNaming(nextAttributeName, objectParameterKey);
            }
        }

        //добавляем в selec-кляузу OBJREFERENCE.REFERENCE(т.е. айди объекта который вставляется в референс)

        if (currentNode.hasReferencedObjects()) {
            int i = 1;
            for (EntityReferenceTaskData reference : currentNode.getCurrentEntityReferenceTasks().values()) {
                String nextReferenceName = referenceNameGenerator.getNextTableName();
                String targetName = "R_" + nextReferenceName;
                queryAppender.appendSelectColumnWithNaming(targetName, config.getOref(), targetName);
                EntityAttrIdType type = new EntityAttrIdType(reference.getInnerClass().getName(), reference.getReferenceAttrId());
                objReferenceConnections.put(targetName, type);
                i++;
            }
        }

        //Начинаем аппендить раздел FROM
        queryAppender.appendFromWord();
        queryAppender.appendFromTableWithNaming(config.getObjectsTableName(), config.getRootTableName());
        queryAppender.appendFromTableWithNaming(config.getObjTypesTableName(), config.getRootTypesTableName());

        //Аппендим таблицы ATTRIBUTES и ATTRTYPES, сколько есть аттрибутов, столько раз и аппендим, прибавляя к имени таблиц по единице
        for (int i = 1; i <= attributesTablesNameGenerator.getTablesCounter(); i++) {
            queryAppender.appendFromTableWithNaming(config.getAttributesTableName(), config.getAttributesPermanentTableName() + i);
            queryAppender.appendFromTableWithNaming(config.getAttrTypesTableName(), config.getAttrTypesPermanentTableName() + i);
        }

        if (currentNode.hasReferencedObjects()) {
            for (int i = 1; i <= referenceNameGenerator.getTablesCounter(); i++) {
                queryAppender.appendFromTableWithNaming(config.getAttrTypesTableName(), "AT_" + config.getReferenceTypePermanentTableName() + i);
                queryAppender.appendFromTableWithNaming(config.getObjTypesTableName(), "OT_" + config.getReferenceTypePermanentTableName() + i);
                queryAppender.appendFromTableWithNaming(config.getObjRefsTableName(), "R_" + config.getReferenceTypePermanentTableName() + i);
            }
        }

        //Аппендим раздел WHERE
        //OB_TY_ID = OBJECT_TYPE_ID
        //OB_ID = OBJECT_ID
        //AT_ID = ATTR_ID
        queryAppender.appendWhereWord();
        queryAppender.appendWhereConditionWithTableCodeEqualsToConstant(config.getRootTypesTableName(), config.getEntityTypeIdConstant());
        queryAppender.appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(config.getRootTableName(), config.getRootTypesTableName());

        //Аппендим аттрибуты
        currentNodeVariablesMapIterator = permanentCurrentNodeVariables.entrySet().iterator();
        for (int i = 1; i <= attributesTablesNameGenerator.getTablesCounter(); i++) {

            // WHERE OBJECTS.OBJECT_ID=ATTRIBUTES.OBJECT_ID
            queryAppender.appendWhereConditionWithTwoTablesEqualsByOB_ID(config.getRootTableName(), config.getAttributesPermanentTableName() + i);
            // WHERE OBJECTS.OBJECT_TYPE_ID=ATTRTYPES.OBJECT_TYPE_ID
            queryAppender.appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(config.getRootTableName(), config.getAttrTypesPermanentTableName() + i);
            //WHERE ATTRIBUTES.ATTR_ID=ATTRTYPES.ATTR_ID
            queryAppender.appendWhereConditionWithTwoTablesEqualsByAT_ID(config.getAttributesPermanentTableName() + i, config.getAttrTypesPermanentTableName() + i);

            Map.Entry<String, EntityVariablesData> objectPropertiesMapIteratorEntry = (Map.Entry) currentNodeVariablesMapIterator.next();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseNativeCodeValue();
            Integer databaseAttrIdValue = objectPropertiesMapIteratorEntry.getValue().getDatabaseAttrtypeIdValue();

            //WHERE ATTRTYPE.CODE=Код из таблицы кодов полйе объекта
            if (null!=databaseColumnValue){
                queryAppender.appendWhereConditionWithTableCodeEqualsToValue(config.getAttrTypesPermanentTableName() + i, databaseColumnValue.substring(1));
            } else {
                queryAppender.appendWhereConditionWithTablesAttrEqualsToValue(config.getAttrTypesPermanentTableName() + i, databaseAttrIdValue);
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
                queryAppender.appendWhereConditionWithTwoTablesEqualsByOB_TY_ID(config.getRootTableName(), attrTypeName);
                queryAppender.appendWhereConditionWithAttrTypeRefEqualsOB_TY_ID(attrTypeName, objTypeName);
                queryAppender.appendWhereConditionWithTwoTablesEqualsByAT_ID(attrTypeName, objReferenceName);
                queryAppender.appendWhereConditionWithTableCodeEqualsToConstant(objTypeName, objTypelist.get(j).getInnerClassObjectTypeName());
                if (null != objTypelist.get(j).getAttrId()) {
                    queryAppender.appendWhereConditionWithObRefAttrIdEqualsInteger(attrTypeName, objTypelist.get(j).getAttrId());
                }
                j++;
                queryAppender.appendWhereConditionWithTwoTablesEqualsByOB_ID(config.getRootTableName(), objReferenceName);
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
        if (isSearchById) queryAppender.appendWhereConditionWithRootTableObjectIdSearching();

        //Сортируем по колонке
        if (null != orderingParameter) {
            queryAppender.appendOrderBy(attributesNameMap.get(orderingParameter), ascend);
        } else queryAppender.closeQueryBuilder();

        return queryBuilder.toString();
    }
}
