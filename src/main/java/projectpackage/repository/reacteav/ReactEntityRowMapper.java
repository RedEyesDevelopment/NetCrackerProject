package projectpackage.repository.reacteav;

import lombok.extern.log4j.Log4j;
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

@Log4j
public class ReactEntityRowMapper implements RowMapper {
    private Class clazz;
    private ReacTask task;
    private LinkedHashMap<String, EntityVariablesData> parameters;
    private HashMap<Integer, EntityReferenceTaskData> referenceData;
    private String dataStringPrefix;

    ReactEntityRowMapper(ReacTask task, String dataStringPrefix) {
        this.task = task;
        this.clazz = task.getObjectClass();
        this.parameters = task.getCurrentEntityParameters();
        this.referenceData = task.getCurrentEntityReferenceTasks();
        this.dataStringPrefix = dataStringPrefix;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Object targetReacEntityObject = null;
        try {
            targetReacEntityObject = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e);
        }

        for (Map.Entry<String, EntityVariablesData> entry : parameters.entrySet()) {
            String objectParameterKey = entry.getKey();
            Class newObjectClass = entry.getValue().getParameterClass();
            Field field = null;
            try {
                field = targetReacEntityObject.getClass().getDeclaredField(objectParameterKey);
            } catch (NoSuchFieldException e) {
                log.error(e);
            }
            boolean fieldWasPrivate = false;
            if (!field.isAccessible()) {
                field.setAccessible(true);
                fieldWasPrivate = true;
            }
            Class throwedClass = null;
            try {
                if (objectParameterKey.equals("objectId")) {
                    task.addIdForChildFetches(resultSet.getInt(objectParameterKey));
                    for (Map.Entry<Integer, EntityReferenceTaskData> data : referenceData.entrySet()) {
                        Integer referenceLinkName = resultSet.getInt(data.getValue().getInnerIdParameterNameForQueryParametersMap());
                        Integer objectKeyId = resultSet.getInt(objectParameterKey);
                        EntityReferenceIdRelation relation = new EntityReferenceIdRelation(data.getKey(), objectKeyId, referenceLinkName, data.getValue().getInnerClass());
                        task.addReferenceIdRelations(task.getReferenceIdRelations().size()+1, relation);
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
                    java.sql.Timestamp date = resultSet.getTimestamp(objectParameterKey+dataStringPrefix);
                    if (null!=date){
                        Date newDate = new Date(date.getTime());
                        field.set(targetReacEntityObject, newDate);
                    }
                }
                if (newObjectClass.equals(Boolean.class)) {
                    throwedClass = Date.class;
                    String bool = resultSet.getString(objectParameterKey);
                    field.set(targetReacEntityObject, Boolean.parseBoolean(bool));
                }
            } catch (IllegalAccessException e) {
                throw new WrongTypeClassException(field.getDeclaringClass(), throwedClass);
            }
            if (fieldWasPrivate) {
                field.setAccessible(false);
            }
        }
        return targetReacEntityObject;
    }

}
