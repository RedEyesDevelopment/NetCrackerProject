package projectpackage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.User;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

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
    public void updateUser(User newUser, User oldUser) {
        String queryTemplate = "UPDATE ATTRIBUTES SET VALUE = '%s' WHERE OBJECT_ID = %d AND ATTR_ID = %d";
        if (!oldUser.getEmail().equals(newUser.getEmail())) {
            jdbcTemplate.execute(String.format(queryTemplate, newUser.getEmail(), newUser.getObjectId(), 15));
        }
        if (!oldUser.getPassword().equals(newUser.getPassword())) {
            jdbcTemplate.execute(String.format(queryTemplate, newUser.getPassword(), newUser.getObjectId(), 16));
        }
        if (!oldUser.getFirstName().equals(newUser.getFirstName())) {
            jdbcTemplate.execute(String.format(queryTemplate, newUser.getFirstName(), newUser.getObjectId(), 17));
        }
        if (!oldUser.getLastName().equals(newUser.getLastName())) {
            jdbcTemplate.execute(String.format(queryTemplate, newUser.getLastName(), newUser.getObjectId(), 18));
        }
        if (!oldUser.getLastName().equals(newUser.getLastName())) {
            jdbcTemplate.execute(String.format(queryTemplate, newUser.getLastName(), newUser.getObjectId(), 19));
        }
        if (oldUser.getRole().getObjectId() != newUser.getRole().getObjectId())
        jdbcTemplate.execute(String.format("UPDATE ATTRIBUTES SET REFERENCE = %d WHERE OBJECT_ID = %d AND ATTR_ID = %d",
                newUser.getRole().getObjectId(), newUser.getObjectId(), 20));
    }

}
