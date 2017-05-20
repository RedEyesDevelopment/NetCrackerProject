package projectpackage.repository.reacteav.annotations;

import java.lang.annotation.*;

@Repeatable(value = References.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReactReference {
    String referenceName();
    Class outerEntityClass();
    String attrIdField() default "";
    String outerFieldName();
    String outerFieldKey() default "objectId";
    String innerFieldKey() default "objectId";
}
