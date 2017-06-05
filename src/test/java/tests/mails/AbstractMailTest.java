package tests.mails;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import tests.AbstractTest;

/**
 * Created by Gvozd on 06.01.2017.
 */
@ContextConfiguration(classes = {TestMailConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ComponentScan(basePackages = {"projectpackage.service.pdfandmail"})
public abstract class AbstractMailTest extends AbstractTest {
    protected final String SEPARATOR = "**********************************************************";
}
