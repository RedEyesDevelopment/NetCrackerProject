package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.security.AuthCredentials;
import projectpackage.repository.securitydao.AuthCredentialsDAO;

import static org.junit.Assert.*;

public class AuthCredentialsTests extends AbstractDatabaseTest{
    private final String SEPARATOR = "**********************************************************";

    @Autowired
    AuthCredentialsDAO authCredentialsDAO;

    @Test
    public void getCredentials(){
        String login = "stephenking@mail.ru";
        AuthCredentials credentials = authCredentialsDAO.getUserByUsername(login);
        System.out.println(credentials);
        System.out.println(SEPARATOR);
        assertNotNull(credentials);
    }

    @Test
    public void getNullCredentials(){
        AuthCredentials credentials = authCredentialsDAO.getUserByUsername(null);
        System.out.println(credentials);
        System.out.println(SEPARATOR);
        assertEquals(null, credentials);
    }

}
