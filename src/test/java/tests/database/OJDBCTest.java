package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Created by Lenovo on 09.04.2017.
 */
public class OJDBCTest {

    private static final Logger LOGGER = Logger.getLogger(OJDBCTest.class);

    @Test
    public void testJDBCConnection(){
        Locale.setDefault(Locale.ENGLISH);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            LOGGER.error("NO ORACLE DRIVER");
        }

        LOGGER.info("There is an ORACLE DRIVER!");

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "Hotel", "root");
        } catch (SQLException e){
            LOGGER.error("CONNECTION FAILED");
        }

        if (connection!=null){
            LOGGER.info("CONNECTION SUCCESS!");
        } else {
            LOGGER.info("NO VALID CONNECTION");
        }
        assertEquals(true, null!=connection);
    }
}
