package projectpackage.repository.reacteav.support;

import org.springframework.stereotype.Component;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.exceptions.WrongEntityClassException;

@Component
public class ReactEntityValidator {
    private static final Class ENTITYANNOTATION = ReactEntity.class;

    public void isTargetClassAReactEntity(Class clazz){
        if (!clazz.isAnnotationPresent(ENTITYANNOTATION)) {
            throw new WrongEntityClassException(null, clazz);
        }
    }
}
