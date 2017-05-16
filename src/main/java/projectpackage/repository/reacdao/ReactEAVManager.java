package projectpackage.repository.reacdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import projectpackage.repository.reacdao.annotations.ReactAnnDefinitionReader;
import projectpackage.repository.reacdao.exceptions.WrongEntityClassException;
import projectpackage.repository.reacdao.support.ReactConnectionsDataBucket;
import projectpackage.repository.reacdao.support.ReactConstantConfiguration;
import projectpackage.repository.reacdao.support.ReactEntityValidator;

@Component
public class ReactEAVManager {

    @Autowired
    ReactEntityValidator validator = new ReactEntityValidator();

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private ReactConstantConfiguration reactConstantConfiguration;
    private ReactConnectionsDataBucket dataBucket;

    public ReactEAVManager(ReactConstantConfiguration reactConstantConfiguration, ReactAnnDefinitionReader definitionReader) {
        this.reactConstantConfiguration = reactConstantConfiguration;

        this.dataBucket =  new ReactConnectionsDataBucket(definitionReader.makeClassesMap(), definitionReader.makeObjectsVariablesMap(), definitionReader.makeOuterRelationshipsMap(), definitionReader.makeObjectsReferenceRelationsMap());
        System.out.println("");
    }

    public ReactEAV createReactEAV(Class entityClass) {
        validator.isTargetClassAReactEntity(entityClass);
        if (null != entityClass) {
            ReactEAV reactEAV = new ReactEAV(entityClass, namedParameterJdbcTemplate, reactConstantConfiguration, dataBucket);
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
