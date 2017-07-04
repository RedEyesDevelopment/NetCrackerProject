package projectpackage.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.support.ReactConstantConfiguration;
import projectpackage.repository.reacteav.support.ReactEntityValidator;
import projectpackage.repository.support.RollbackableTransactionalCustomizer;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Locale;

@Configuration
@EnableTransactionManagement
public class JDBCConfiguration implements TransactionManagementConfigurer {

    private static final Logger LOGGER = Logger.getLogger(JDBCConfiguration.class);

    @Value("${dataSource.driverClassName}")
    private String driver;
    @Value("${dataSource.url}")
    private String url;
    @Value("${dataSource.username}")
    private String username;
    @Value("${dataSource.password}")
    private String password;
    @Value("${model.package.path}")
    private String modelPackage;

    public JDBCConfiguration() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Driver not found!", e);
        }
        try {
            comboPooledDataSource.setDriverClass(driver);
        } catch (PropertyVetoException e) {
            LOGGER.error("Driver not supported!", e);
        }
        //ссылка
        comboPooledDataSource.setJdbcUrl(url);
        //логин
        comboPooledDataSource.setUser(username);
        //пароль
        comboPooledDataSource.setPassword(password);
        //минимальный размер пула
        comboPooledDataSource.setMinPoolSize(5);
        //максимальный размер пула
        comboPooledDataSource.setMaxPoolSize(40);
        //начальный размер пула
        comboPooledDataSource.setInitialPoolSize(10);
        //сколько пулов разрешено взять поверх максимального числа
        comboPooledDataSource.setAcquireIncrement(10);
        //максимальное время получения содеинения под запрос
        comboPooledDataSource.setMaxIdleTime(300);
        //максимальное время жизни запроса
        comboPooledDataSource.setMaxConnectionAge(1200);
        //время простоя соединения, после которого оно уничтожается, пул сжимается до минимума
        comboPooledDataSource.setMaxIdleTimeExcessConnections(120);
        //время между повторами запроса на соединение
        comboPooledDataSource.setAcquireRetryDelay(1500);
        //размер кэша под preparestatements
        comboPooledDataSource.setMaxStatements(500);
        //размер кэша для одного соединения под preparestatements
        comboPooledDataSource.setMaxStatementsPerConnection(14);
        //время через которое проверяется соединение на состояние
        comboPooledDataSource.setIdleConnectionTestPeriod(300);
        //имя специальной таблицы для тестирования соединения с БД
        comboPooledDataSource.setAutomaticTestTable("c3p0DatabaseTestTable");
        comboPooledDataSource.setForceIgnoreUnresolvedTransactions(true);
        comboPooledDataSource.setAutoCommitOnClose(false);
        comboPooledDataSource.setConnectionCustomizerClassName(RollbackableTransactionalCustomizer.class.getName());
        comboPooledDataSource.setNumHelperThreads(10);

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
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    JdbcTemplate jdbcTemplate () { return new JdbcTemplate(dataSource()); }

    @Bean
    ReactConstantConfiguration reactConstantConfiguration() { return new ReactConstantConfiguration(); }

    @Bean
    ReactEAVManager reactEAVManager(){
        return new ReactEAVManager(reactConstantConfiguration(),modelPackage);
    }

    @Bean
    ReactEntityValidator reactEntityValidator(){
        return new ReactEntityValidator();
    }

}