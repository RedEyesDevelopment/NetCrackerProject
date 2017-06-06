package projectpackage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import projectpackage.aspects.ModificationHistoryAspect;
import projectpackage.aspects.NotificationInsertOrChangeAspect;

/**
 * Created by Lenovo on 29.05.2017.
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfiguration {

    @Bean
    ModificationHistoryAspect modificationHistoryAspect(){
        return new ModificationHistoryAspect();
    }

    @Bean
    NotificationInsertOrChangeAspect notificationCreationAspect(){
        return new NotificationInsertOrChangeAspect();
    }
}
