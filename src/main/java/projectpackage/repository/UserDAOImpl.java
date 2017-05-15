package projectpackage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.reacdao.ReactEAVManager;
import projectpackage.repository.reacdao.exceptions.ResultEntityNullException;

import java.util.*;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ReactEAVManager manager;

    @Override
    public Map<Integer,User> getAllUsers(String orderParameter) {
        String sqlQuery = "SELECT USERS.OBJECT_ID \"objectId\", USERLOG.VALUE \"email\", USERPASS.VALUE \"password\",\n" +
                "USERFIN.VALUE \"firstName\", USERLAN.VALUE \"lastName\", USERAD.VALUE \"additionalInfo\",\n" +
                "USEROLE.VALUE \"role\"\n" +
                "FROM OBJECTS USERS, ATTRIBUTES USERLOG, ATTRIBUTES USERPASS,\n" +
                "ATTRIBUTES USERFIN, ATTRIBUTES USERLAN, ATTRIBUTES USERAD, ATTRIBUTES USEROLE,\n" +
                "OBJREFERENCE ROLEREF\n" +
                "WHERE USERS.OBJECT_TYPE_ID=3 AND\n" +
                "    USERLOG.ATTR_ID=15 AND\n" +
                "    USERPASS.ATTR_ID=16 AND\n" +
                "    USERFIN.ATTR_ID=17 AND\n" +
                "    USERLAN.ATTR_ID=18 AND\n" +
                "    USERAD.ATTR_ID=19 AND\n" +
                "    USEROLE.ATTR_ID=39 AND\n" +
                "    ROLEREF.ATTR_ID=20 AND\n" +
                "    ROLEREF.OBJECT_ID=USERS.OBJECT_ID AND\n" +
                "    ROLEREF.REFERENCE=USEROLE.OBJECT_ID AND\n" +
                "    USERS.OBJECT_ID=USERLOG.OBJECT_ID AND\n" +
                "    USERS.OBJECT_ID=USERPASS.OBJECT_ID AND\n" +
                "    USERS.OBJECT_ID=USERFIN.OBJECT_ID AND\n" +
                "    USERS.OBJECT_ID=USERLAN.OBJECT_ID AND\n" +
                "    USERS.OBJECT_ID=USERAD.OBJECT_ID\n" +
                "    ORDER BY :orderParameter";
        SqlParameterSource namedParameters = new MapSqlParameterSource("orderParameter", orderParameter);
        List<User> list = namedParameterJdbcTemplate.query(sqlQuery,namedParameters, new BeanPropertyRowMapper<User>(User.class));
        TreeMap<Integer,User> map = new TreeMap<>();
        for (User user: list){
            map.put(user.getObjectId(), user);
        }
    return map;
    }

    @Override
    public void insertUser(User user) {
        String intoObjects = String.format("INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) " +
                "VALUES (%d, NULL, 3, '%s', NULL)", user.getObjectId(), user.getLastName());
        String intoAttributes1 = String.format("INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) " +
                "VALUES (15,%d,'%s',NULL)", user.getObjectId(), user.getEmail());
        String intoAttributes2 = String.format("INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) " +
                "VALUES (16,%d,'%s',NULL)", user.getObjectId(), user.getPassword());
        String intoAttributes3 = String.format("INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) " +
                "VALUES (17,%d,'%s',NULL)", user.getObjectId(), user.getFirstName());
        String intoAttributes4 = String.format("INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) " +
                "VALUES (18,%d,'%s',NULL)", user.getObjectId(), user.getLastName());
        String intoAttributes5 = String.format("INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) " +
                "VALUES (19,%d,'%s',NULL)", user.getObjectId(), user.getAdditionalInfo());
        String intoReferences = String.format("INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) " +
                "VALUES (20,%d,%d)", user.getObjectId(), user.getRole().getObjectId());
        //оставлю комменты чтобы не забыть по поводу автокоммитов когда/если будем переделывать
        //здесь по идее начинаем транзакцию
        jdbcTemplate.execute(intoObjects);
        jdbcTemplate.execute(intoAttributes1);
        jdbcTemplate.execute(intoAttributes2);
        jdbcTemplate.execute(intoAttributes3);
        jdbcTemplate.execute(intoAttributes4);
        jdbcTemplate.execute(intoAttributes5);
        jdbcTemplate.execute(intoReferences);
        //а здесь коммитим если все окей
    }

    /**
     * Метод получает нового юзера и вытаскивает старого, затем сравнивает их поля и выполняет запросы
     */
    @Override
    public void updateUser(User user) {
        User oldUser = getOldUser(user.getObjectId());
        String queryTemplate = "UPDATE ATTRIBUTES SET VALUE = '%s' WHERE OBJECT_ID = %d AND ATTR_ID = %d";
        if (!oldUser.getEmail().equals(user.getEmail())) {
            jdbcTemplate.execute(String.format(queryTemplate, user.getEmail(), user.getObjectId(), 15));
        }
        if (!oldUser.getPassword().equals(user.getPassword())) {
            jdbcTemplate.execute(String.format(queryTemplate, user.getPassword(), user.getObjectId(), 16));
        }
        if (!oldUser.getFirstName().equals(user.getFirstName())) {
            jdbcTemplate.execute(String.format(queryTemplate, user.getFirstName(), user.getObjectId(), 17));
        }
        if (!oldUser.getLastName().equals(user.getLastName())) {
            jdbcTemplate.execute(String.format(queryTemplate, user.getLastName(), user.getObjectId(), 18));
        }
        if (!oldUser.getLastName().equals(user.getLastName())) {
            jdbcTemplate.execute(String.format(queryTemplate, user.getLastName(), user.getObjectId(), 19));
        }
        if (oldUser.getRole().getObjectId() != user.getRole().getObjectId())
        jdbcTemplate.execute(String.format("UPDATE ATTRIBUTES SET REFERENCE = %d WHERE OBJECT_ID = %d AND ATTR_ID = %d",
                user.getRole().getObjectId(), user.getObjectId(), 20));
    }

    /**
     * Метод который возвращает старого юзера, который хранится в бд
     */
    private User getOldUser(int objectId) {
        User oldUser = null;
        try {
            oldUser = (User) manager.createReactEAV(User.class).fetchInnerEntityCollection(Role.class).closeFetch().getSingleEntityWithId(objectId);
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }

        return oldUser;
    }

}
