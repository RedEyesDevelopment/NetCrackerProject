package tests.database;

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

    @Test
    public void testJDBCConnection(){
        Locale.setDefault(Locale.ENGLISH);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("NO ORACLE DRIVER");
            e.printStackTrace();
        }

        System.out.println("There is an ORACLE DRIVER!");

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "RestTest", "root");
        } catch (SQLException e){
            System.out.println("CONNECTION FAILED");
            e.printStackTrace();
        }

        if (connection!=null){
            System.out.println("CONNECTION SUCCESS!");
        } else {
            System.out.println("NO VALID CONNECTION");
        }
        assertEquals(true, null!=connection);
    }
}
