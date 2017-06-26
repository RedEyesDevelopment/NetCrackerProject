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
    private ReactConstantConfiguration reactConstantConfiguration;
    private ReactConnectionsDataBucket dataBucket;

    public ReactEAVManager(ReactConstantConfiguration configuration, String modelsPackage) {
        ReactAnnDefinitionReader reactAnnDefinitionReader = new ReactAnnDefinitionReader(modelsPackage);
        this.reactConstantConfiguration=configuration;
        this.dataBucket = new ReactConnectionsDataBucket(reactAnnDefinitionReader.makeClassesMap(), reactAnnDefinitionReader.makeObjectsVariablesMap(), reactAnnDefinitionReader.makeOuterRelationshipsMap(), reactAnnDefinitionReader.makeObjectsReferenceRelationsMap());
    }

    public ReactEAV createReactEAV(Class entityClass) {
        validator.isTargetClassAReactEntity(entityClass);
        if (null != entityClass) {
            ReactQueryBuilder builder = new ReactQueryBuilder(reactConstantConfiguration);
            ReacQueryTasksPreparator preparator = new ReacQueryTasksPreparator(reactConstantConfiguration,dataBucket);
            return new ReactEAV(entityClass, namedParameterJdbcTemplate, reactConstantConfiguration, dataBucket, builder,preparator,validator);
        } else {
            throw new WrongEntityClassException("Null is not a valid object");
        }
    }
}
