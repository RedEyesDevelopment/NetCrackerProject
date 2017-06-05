package tests.customtests;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import tests.AbstractTest;

/**
 * Created by Gvozd on 06.01.2017.
 */
@Configuration
@ContextConfiguration(classes = {TestFileConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ComponentScan(basePackages = {"projectpackage.service.fileservice"})
@IntegrationComponentScan
public abstract class AbstractFileTest extends AbstractTest{
    protected final String SEPARATOR = "**********************************************************";
}
