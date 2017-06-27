package tests.regexes;

import org.junit.Test;
import projectpackage.service.regex.RegexService;
import projectpackage.service.regex.RegexServiceImpl;
import tests.AbstractTest;

import static org.junit.Assert.assertEquals;

/**
 * Created by Lenovo on 02.05.2017.
 */

public class RegexTest extends AbstractTest{
    @Test
    public void doTest(){
        RegexService service = new RegexServiceImpl();
        assertEquals(true, service.isValidPhone("+380676832268"));
        assertEquals(true, service.isValidPhone("0676832268"));
        assertEquals(true, service.isValidPhone("7471409"));
        assertEquals(true, service.isValidPhone("+38(067)68-32-268"));
        assertEquals(false, service.isValidPhone("+3dfssdv80dsfv67dv6832268"));
        assertEquals(false, service.isValidPhone("dsfvsevwefvwev"));
        assertEquals(false, service.isValidPhone("471409"));
        assertEquals(false, service.isValidPhone("+3dfssdv80dsfv67dv6832268"));
        assertEquals(false, service.isValidEmail("nfdsfdsfds32@ru"));
        assertEquals(true, service.isValidEmail("sasha-merlyan@yandex.ru"));
    }
}
