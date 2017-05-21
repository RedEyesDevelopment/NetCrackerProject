package tests.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import projectpackage.repository.authdao.*;
import projectpackage.repository.blocksdao.BlockDAO;
import projectpackage.repository.blocksdao.BlockDAOImpl;
import projectpackage.repository.maintenancedao.JournalRecordDAO;
import projectpackage.repository.notificationsdao.NotificationDAO;
import projectpackage.repository.notificationsdao.NotificationDAOImpl;
import projectpackage.repository.notificationsdao.NotificationTypeDAO;
import projectpackage.repository.notificationsdao.NotificationTypeDAOImpl;
import projectpackage.repository.ordersdao.ModificationHistoryDAO;
import projectpackage.repository.ordersdao.OrderDAO;
import projectpackage.repository.ordersdao.OrderDAOImpl;
import projectpackage.repository.ratesdao.PriceDAO;
import projectpackage.repository.ratesdao.PriceDAOImpl;
import projectpackage.repository.ratesdao.RateDAO;
import projectpackage.repository.ratesdao.RateDAOImpl;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.support.ReactAnnDefinitionReader;
import projectpackage.repository.reacteav.support.ReactConstantConfiguration;
import projectpackage.repository.reacteav.support.ReactEntityValidator;
import projectpackage.repository.roomsdao.RoomDAO;
import projectpackage.repository.roomsdao.RoomDAOImpl;
import projectpackage.repository.securitydao.AuthCredentialsDAO;
import projectpackage.repository.securitydao.AuthCredentialsDAOImpl;
import projectpackage.service.authservice.PhoneService;
import projectpackage.service.authservice.PhoneServiceImpl;
import projectpackage.service.authservice.RoleService;
import projectpackage.service.authservice.RoleServiceImpl;
import projectpackage.service.notificationservice.NotificationService;
import projectpackage.service.notificationservice.NotificationServiceImpl;
import projectpackage.service.orderservice.OrderService;
import projectpackage.service.orderservice.OrderServiceImpl;
import projectpackage.service.rateservice.PriceService;
import projectpackage.service.rateservice.PriceServiceImpl;
import projectpackage.service.securityservice.SecurityService;
import projectpackage.service.securityservice.SecurityServiceImpl;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

//import projectpackage.service.orderservice.ModificationHistoryServiceImpl;

//import projectpackage.repository.roomsdao.RoomTypeDAOImpl;

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
    private String modelPackage;

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
        driver = props.getProperty("dataSource.driverClassName");
        url = props.getProperty("dataSource.url");
        username = props.getProperty("dataSource.username");
        password = props.getProperty("dataSource.password");
        modelPackage = props.getProperty("model.package.path");
    }

    @Bean
    public DataSource dataSource() {
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
        comboPooledDataSource.setAutoCommitOnClose(false);

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
    NamedParameterJdbcTemplate namedParameteJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    JdbcTemplate jdbcTemplate () { return new JdbcTemplate(dataSource()); }

    @Bean
    UserDAO userDAO() {
        return new UserDAOImpl();
    }

    @Bean
    PhoneDAO phoneDAO() {
        return new PhoneDAOImpl();
    }

    @Bean
    RoleDAO roleDAO() { return new RoleDAOImpl();}

    @Bean
    ModificationHistoryDAO modificationHistoryDAO() {return null;}

    @Bean
    JournalRecordDAO journalRecordDAO() {return  null;}

    @Bean
    NotificationDAO notificationDAO(){
        return new NotificationDAOImpl();
    }

    @Bean
    NotificationTypeDAO notificationTypeDAO(){
        return new NotificationTypeDAOImpl();
    }

    @Bean
    RoomDAO roomDAO(){
        return new RoomDAOImpl();
    }

//    @Bean
//    RoomTypeDAO roomTypeDAO(){
//        return new RoomTypeDAOImpl();
//    }

    @Bean
    PriceDAO priceDAO(){
        return new PriceDAOImpl();
    }

    @Bean
    RateDAO rateDAO(){
        return new RateDAOImpl();
    }

    @Bean
    OrderDAO orderDAO(){
        return new OrderDAOImpl();
    }

    @Bean
    BlockDAO blockDAO(){
        return new BlockDAOImpl();
    }

//    @Bean
//    UserService userService() {
//        return new UserServiceImpl();
//    }

    @Bean
    RoleService roleService() {
        return new RoleServiceImpl();
    }

    @Bean
    PhoneService phoneService() {
        return new PhoneServiceImpl();
    }

//    @Bean
//    BlockService blockService() {
//        return new BlockServiceImpl();
//    }

    @Bean
    NotificationService notificationService() {
        return new NotificationServiceImpl();
    }

//    @Bean
//    NotificationTypeService notificationTypeService() {
//        return new NotificationTypeServiceImpl();
//    }

    @Bean
    OrderService orderService() {
        return new OrderServiceImpl();
    }

//    @Bean
//    ModificationHistoryService modificationHistoryService() {
//        return new ModificationHistoryServiceImpl();
//    }

//    @Bean
//    RoomService roomService() {
//        return new RoomServiceImpl();
//    }
//
//    @Bean
//    RoomTypeService roomTypeService() {
//        return new RoomTypeServiceImpl();
//    }

    @Bean
    PriceService priceService() {
        return new PriceServiceImpl();
    }

//    @Bean
//    RateService rateService() {
//        return new RateServiceImpl();
//    }

    @Bean
    ReactConstantConfiguration reactConstantConfiguration() { return new ReactConstantConfiguration(); }

    @Bean
    ReactAnnDefinitionReader reactAnnDefinitionReader(){
        return new ReactAnnDefinitionReader(modelPackage);
    }

    @Bean
    ReactEAVManager reactEAVManager(){
        return new ReactEAVManager(reactConstantConfiguration(),reactAnnDefinitionReader());
    }

    @Bean
    ReactEntityValidator reactEntityValidator(){
        return new ReactEntityValidator();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
        return encoder;
    }

    @Bean
    AuthCredentialsDAO authCredentialsDAO() { return new AuthCredentialsDAOImpl(); }

    @Bean
    SecurityService securityService() {return new SecurityServiceImpl(); }
}
