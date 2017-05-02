package tests.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import projectpackage.repository.ModelRepository;
import projectpackage.repository.ModelRepositoryImpl;
import projectpackage.service.ModelService;
import projectpackage.service.ModelServiceImpl;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by Gvozd on 06.01.2017.
 */
@ContextConfiguration
@EnableTransactionManagement
public class TestJPAConfig implements TransactionManagementConfigurer {

    private String driver;
    private String url;
    private String username;
    private String password;

    public TestJPAConfig() {
        Locale.setDefault(Locale.ENGLISH);
        Properties props = new Properties();
        FileInputStream fis = null;

        String fileName = "application.properties";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver=props.getProperty("dataSource.driverClassName");
        url=props.getProperty("dataSource.url");
        username=props.getProperty("dataSource.username");
        password=props.getProperty("dataSource.password");
    }

    @Bean
    public DataSource dataSource(){
        Log4jdbcProxyDataSource dataSource = new Log4jdbcProxyDataSource(realDataSource());
        Log4JdbcCustomFormatter log4JdbcCustomFormatter = new Log4JdbcCustomFormatter();
        log4JdbcCustomFormatter.setLoggingType(LoggingType.SINGLE_LINE);
        log4JdbcCustomFormatter.setSqlPrefix("SQL:::");
        dataSource.setLogFormatter(log4JdbcCustomFormatter);
        return dataSource;
    }

    @Bean
    public DataSource realDataSource() {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            comboPooledDataSource.setDriverClass(driver);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        comboPooledDataSource.setJdbcUrl(url);
        comboPooledDataSource.setUser(username);
        comboPooledDataSource.setPassword(password);

        return comboPooledDataSource;
    }

    @Override
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

    @Bean
    NamedParameterJdbcTemplate jdbcTemplate(){
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    ModelRepository modelRepository(){
        return new ModelRepositoryImpl();
    }

    @Bean
    ModelService modelService(){
        return new ModelServiceImpl();
    }
}
