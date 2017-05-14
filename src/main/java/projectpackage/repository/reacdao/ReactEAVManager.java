package projectpackage.repository.reacdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import projectpackage.repository.reacdao.models.ReacEntity;
import projectpackage.repository.reacdao.exceptions.WrongEntityClassException;
import projectpackage.repository.reacdao.support.ReactConstantConfiguration;

@Component
public class ReactEAVManager {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private ReactConstantConfiguration reactConstantConfiguration;

    public ReactEAVManager(NamedParameterJdbcTemplate namedParameterJdbcTemplate, ReactConstantConfiguration reactConstantConfiguration) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.reactConstantConfiguration = reactConstantConfiguration;
    }

    public void setReactConstantConfiguration(ReactConstantConfiguration reactConstantConfiguration) {
        this.reactConstantConfiguration = reactConstantConfiguration;
    }

    public ReactEAV createReactEAV(Class<? extends ReacEntity> entityClass) {
        if (null != entityClass && ReacEntity.class.isAssignableFrom(entityClass)) {
            return new ReactEAV(entityClass, namedParameterJdbcTemplate, reactConstantConfiguration);
        } else {
            WrongEntityClassException exception;
            if (null == entityClass) {
                exception = new WrongEntityClassException("Null is not a valid object");
            } else exception = new WrongEntityClassException(null, entityClass);
            throw exception;
        }
    }

    ;
}
