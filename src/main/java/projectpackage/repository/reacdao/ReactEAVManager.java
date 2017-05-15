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
    ReactConnectionsDataBucket dataBucket;

    public ReactEAVManager(ReactConstantConfiguration reactConstantConfiguration, ReactAnnDefinitionReader definitionReader) {
        this.reactConstantConfiguration = reactConstantConfiguration;
        dataBucket = createDataBucket(definitionReader);
    }

    private ReactConnectionsDataBucket createDataBucket(ReactAnnDefinitionReader reader) {
        System.out.println("***************************************************************************************************");
        reader.printClassesList();
        System.out.println("***************************************************************************************************");

        ReactConnectionsDataBucket bucket = new ReactConnectionsDataBucket(reader.makeClassesMap(), reader.makeObjectsVariablesMap(), reader.makeOuterRelationshipsMap(), reader.makeObjectsReferenceRelationsMap());
        return bucket;
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
