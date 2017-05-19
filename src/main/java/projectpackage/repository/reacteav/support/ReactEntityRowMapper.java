package projectpackage.repository.reacteav.support;

import org.springframework.jdbc.core.RowMapper;
import projectpackage.repository.reacteav.exceptions.WrongTypeClassException;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceIdRelation;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceTaskData;
import projectpackage.repository.reacteav.relationsdata.EntityVariablesData;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReactEntityRowMapper implements RowMapper {
    Class clazz;
    LinkedHashMap<String, EntityVariablesData> parameters;
    HashMap<Integer, EntityReferenceTaskData> referenceData;
    HashMap<Integer, EntityReferenceIdRelation> objectReferenceTable;
    String dataStringPrefix;

    public ReactEntityRowMapper(Class entityClass, LinkedHashMap<String, EntityVariablesData> parameters, HashMap<Integer, EntityReferenceTaskData> referenceData, HashMap<Integer, EntityReferenceIdRelation> objectReferenceTable, String dataStringPrefix) {
        clazz = entityClass;
        this.parameters = parameters;
        this.referenceData = referenceData;
        this.objectReferenceTable = objectReferenceTable;
        this.dataStringPrefix = dataStringPrefix;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Object targetReacEntityObject = null;
        try {
            targetReacEntityObject = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, EntityVariablesData> entry : parameters.entrySet()) {
            String objectParameterKey = entry.getKey();
            Class newObjectClass = entry.getValue().getParameterClass();
            Field field = null;
            try {
                field = targetReacEntityObject.getClass().getDeclaredField(objectParameterKey);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            boolean fieldWasPrivate = false;
            if (!field.isAccessible()) {
                field.setAccessible(true);
                fieldWasPrivate = true;
            }
            Class throwedClass = null;
            try {
                if (objectParameterKey.equals("objectId")) {
                    for (Map.Entry<Integer, EntityReferenceTaskData> data : referenceData.entrySet()) {
                        Integer referenceLinkName = resultSet.getInt(data.getValue().getInnerIdParameterNameForQueryParametersMap());
                        Integer objectId = resultSet.getInt(objectParameterKey);
                        EntityReferenceIdRelation relation = new EntityReferenceIdRelation(objectId, referenceLinkName, data.getValue().getInnerClass());
                        objectReferenceTable.put(data.getKey(), relation);
                    }
                }
                if (newObjectClass.equals(Integer.class)) {
                    throwedClass = Integer.class;
                    field.set(targetReacEntityObject, resultSet.getInt(objectParameterKey));
                }
                if (newObjectClass.equals(String.class)) {
                    throwedClass = String.class;
                    field.set(targetReacEntityObject, resultSet.getString(objectParameterKey));
                }
                if (newObjectClass.equals(Long.class)) {
                    throwedClass = Long.class;
                    field.set(targetReacEntityObject, resultSet.getLong(objectParameterKey));
                }
                if (newObjectClass.equals(Date.class)) {
                    throwedClass = Date.class;
                    Date date = resultSet.getDate(objectParameterKey+dataStringPrefix);
                    field.set(targetReacEntityObject, date);
                }
            } catch (IllegalAccessException e) {
                throw new WrongTypeClassException(field.getDeclaringClass(), throwedClass);
            }
            if (fieldWasPrivate) field.setAccessible(false);
        }
        return targetReacEntityObject;
    }

}
