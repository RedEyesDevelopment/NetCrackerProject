package tests.pdf;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import tests.AbstractTest;

/**
 * Created by Gvozd on 06.01.2017.
 */
@ContextConfiguration(classes = {TestPDFConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public abstract class AbstractPDFTest extends AbstractTest {
    protected final String SEPARATOR = "**********************************************************";
}
