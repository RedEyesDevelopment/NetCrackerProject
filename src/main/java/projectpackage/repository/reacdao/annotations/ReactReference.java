package projectpackage.repository.reacdao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReactReference {
    Class outerEntityClass();
    String outerFieldName() default "";
    String outerFieldKey() default "objectId";
    String innerFieldKey() default "objectId";
}
