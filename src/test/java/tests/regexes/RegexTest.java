package tests.regexes;

import org.junit.Test;
import projectpackage.service.phoneregex.PhoneRegexService;
import projectpackage.service.phoneregex.PhoneRegexServiceImpl;
import tests.AbstractTest;

import static org.junit.Assert.assertEquals;

/**
 * Created by Lenovo on 02.05.2017.
 */

public class RegexTest extends AbstractTest{
    @Test
    public void doTest(){
        PhoneRegexService service = new PhoneRegexServiceImpl();
        assertEquals(true, service.match("+380676832268"));
        assertEquals(true, service.match("0676832268"));
        assertEquals(true, service.match("7471409"));
        assertEquals(true, service.match("+38(067)68-32-268"));
        assertEquals(false, service.match("+3dfssdv80dsfv67dv6832268"));
        assertEquals(false, service.match("dsfvsevwefvwev"));
        assertEquals(false, service.match("471409"));
        assertEquals(false, service.match("+3dfssdv80dsfv67dv6832268"));
    }
}
