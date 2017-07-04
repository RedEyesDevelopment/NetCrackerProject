package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.security.AuthCredentials;
import projectpackage.repository.securitydao.AuthCredentialsDAO;

import static org.junit.Assert.*;

public class AuthCredentialsTests extends AbstractDatabaseTest{

    private static final Logger LOGGER = Logger.getLogger(AuthCredentialsTests.class);

    private final String SEPARATOR = "**********************************************************";

    @Autowired
    AuthCredentialsDAO authCredentialsDAO;

    @Test
    public void getCredentials(){
        String login = "stephenking@mail.ru";
        AuthCredentials credentials = authCredentialsDAO.getUserByUsername(login);
        LOGGER.info(credentials);
        LOGGER.info(SEPARATOR);
        assertNotNull(credentials);
    }

    @Test
    public void getNullCredentials(){
        AuthCredentials credentials = authCredentialsDAO.getUserByUsername(null);
        LOGGER.info(credentials);
        LOGGER.info(SEPARATOR);
        assertEquals(null, credentials);
    }

}
