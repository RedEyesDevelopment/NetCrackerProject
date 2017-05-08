package projectpackage.repository.reacdao.support;

import org.springframework.jdbc.core.RowMapper;
import projectpackage.repository.reacdao.exceptions.WrongTypeClassException;
import projectpackage.repository.reacdao.fetch.EntityVariablesNode;
import projectpackage.repository.reacdao.models.ReacEntity;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReactEntityRowMapper implements RowMapper<ReacEntity> {
    Class<? extends ReacEntity> clazz;
    LinkedHashMap<String, EntityVariablesNode> parameters;
    String dataStringPrefix;

    public ReactEntityRowMapper(Class entityClass, LinkedHashMap<String, EntityVariablesNode> parameters, String dataStringPrefix) {
        clazz = entityClass;
        this.parameters = parameters;
        this.dataStringPrefix = dataStringPrefix;
    }

    @Override
    public ReacEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        ReacEntity targetReacEntityObject = null;
        try {
            targetReacEntityObject = (ReacEntity) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, EntityVariablesNode> entry : parameters.entrySet()) {
            String objectParameterKey = entry.getKey();
            Class newObjectClass = entry.getValue().getParameterClass();
            System.out.println("FIELDCLASS="+newObjectClass.getName());
            Field field = null;
                try {
                    field = targetReacEntityObject.getClass().getDeclaredField(objectParameterKey);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                boolean fieldWasPrivate = false;
                        if (!field.isAccessible()){
                            field.setAccessible(true);
                            fieldWasPrivate = true;
                        }
                Class throwedClass=null;
                try {
                    if (newObjectClass.equals(Integer.class)) {
                        throwedClass=Integer.class;
                        System.out.println("INTEGER="+resultSet.getInt(objectParameterKey));
                        field.set(targetReacEntityObject, resultSet.getInt(objectParameterKey));
                    }
                    if (newObjectClass.equals(String.class)) {
                        throwedClass=String.class;
                        System.out.println("STRING="+resultSet.getString(objectParameterKey));
                        field.set(targetReacEntityObject, resultSet.getString(objectParameterKey));
                    }
                    if (newObjectClass.equals(Long.class)) {
                        throwedClass=Long.class;
                        System.out.println("LONG"+resultSet.getLong(objectParameterKey));
                        field.set(targetReacEntityObject, resultSet.getLong(objectParameterKey));
                    }
                    if (newObjectClass.equals(java.sql.Date.class)) {
                        throwedClass=java.sql.Date.class;
                        System.out.println("Date="+resultSet.getDate(objectParameterKey));
                        Date date = resultSet.getDate(objectParameterKey);
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
