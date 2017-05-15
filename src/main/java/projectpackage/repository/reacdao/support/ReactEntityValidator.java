package projectpackage.repository.reacdao.support;

import org.springframework.stereotype.Component;
import projectpackage.repository.reacdao.annotations.ReactEntity;
import projectpackage.repository.reacdao.exceptions.WrongEntityClassException;

@Component
public class ReactEntityValidator {
    private static final Class ENTITYANNOTATION = ReactEntity.class;

    public void isTargetClassAReactEntity(Class clazz){
        if (!clazz.isAnnotationPresent(ENTITYANNOTATION)) {
            throw new WrongEntityClassException(null, clazz);
        }
    }
}
