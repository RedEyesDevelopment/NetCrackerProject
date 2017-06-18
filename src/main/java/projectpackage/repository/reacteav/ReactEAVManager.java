package projectpackage.repository.reacteav;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import projectpackage.repository.reacteav.support.ReactAnnDefinitionReader;
import projectpackage.repository.reacteav.exceptions.WrongEntityClassException;
import projectpackage.repository.reacteav.support.ReactConnectionsDataBucket;
import projectpackage.repository.reacteav.support.ReactConstantConfiguration;
import projectpackage.repository.reacteav.support.ReactEntityValidator;

@Component
public class ReactEAVManager {

    @Autowired
    private ReactEntityValidator validator;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private ReactConstantConfiguration reactConstantConfiguration;
    @Autowired
    private ReactAnnDefinitionReader reactAnnDefinitionReader;

    private ReactConnectionsDataBucket dataBucket;

    public ReactEAVManager(ReactConstantConfiguration configuration, ReactAnnDefinitionReader reader) {
        this.reactAnnDefinitionReader=reader;
        this.reactConstantConfiguration=configuration;
        this.dataBucket = new ReactConnectionsDataBucket(reactAnnDefinitionReader.makeClassesMap(), reactAnnDefinitionReader.makeObjectsVariablesMap(), reactAnnDefinitionReader.makeOuterRelationshipsMap(), reactAnnDefinitionReader.makeObjectsReferenceRelationsMap());
    }

    public ReactEAV createReactEAV(Class entityClass) {
        validator.isTargetClassAReactEntity(entityClass);
        if (null != entityClass) {
            ReactQueryBuilder builder = new ReactQueryBuilder(reactConstantConfiguration);
            ReactEAV reactEAV = new ReactEAV(entityClass, namedParameterJdbcTemplate, reactConstantConfiguration, dataBucket, builder);
            return reactEAV;
        } else {
            WrongEntityClassException exception;
            if (null == entityClass) {
                exception = new WrongEntityClassException("Null is not a valid object");
            } else exception = new WrongEntityClassException(null, entityClass);
            throw exception;
        }
    }
}
