package projectpackage.repository.reacdao.support;

import org.springframework.jdbc.core.RowMapper;
import projectpackage.repository.reacdao.models.ReacEntity;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class ReactEntityRowMapper implements RowMapper<ReacEntity> {
    Class<? extends ReacEntity> clazz;
    Map<String, String> parameters;
    String dataStringPrefix;

    public ReactEntityRowMapper(Class entityClass, Map<String, String> parameters, String dataStringPrefix) {
        clazz = entityClass;
        this.parameters = parameters;
        this.dataStringPrefix = dataStringPrefix;
    }

    @Override
    public ReacEntity mapRow(ResultSet resultSet, int i) throws SQLException {

        for (int j=0; j<500; j++){
            System.out.println("i="+j+" result="+resultSet.getObject(j));
        }

        ReacEntity reacEntity = null;
        try {
            reacEntity = (ReacEntity) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String objectParameterKey = entry.getKey();
            String databaseColumnValue = entry.getValue();
            if (null != databaseColumnValue) {
                Field field = null;
                try {
                    field = reacEntity.getClass().getDeclaredField(objectParameterKey);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                Class fieldClass = field.getType();
                try {
                    if (fieldClass.equals(String.class)) {
                        field.set(reacEntity, resultSet.getString(databaseColumnValue));
                    }
                    if (fieldClass.equals(Integer.class)) {
                        field.set(reacEntity, resultSet.getInt(databaseColumnValue));
                    }
                    if (fieldClass.equals(Long.class)) {
                        field.set(reacEntity, resultSet.getLong(databaseColumnValue));
                    }
                    if (fieldClass.equals(Date.class)) {
                        Date date = resultSet.getDate(databaseColumnValue);
                        field.set(reacEntity, date);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return reacEntity;
    }
}
