package projectpackage.repository.authdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.User;
import projectpackage.repository.daoexceptions.TransactionException;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void insertUser(User user) throws TransactionException {
        try {
            String insertObjectTemplate = "INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (%d, NULL, 3, '%s', NULL)";
            String insertAttributeTemplate = "INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (%d,%d,'%s',NULL)";
            String insertReferenceTemplate = "INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (%d,%d,%d)";
            String intoObjects = String.format(insertObjectTemplate, user.getObjectId(), user.getLastName());
            String intoAttributes1 = String.format(insertAttributeTemplate, 15, user.getObjectId(), user.getEmail());
            String intoAttributes2 = String.format(insertAttributeTemplate, 16, user.getObjectId(), user.getPassword());
            String intoAttributes3 = String.format(insertAttributeTemplate, 17, user.getObjectId(), user.getFirstName());
            String intoAttributes4 = String.format(insertAttributeTemplate, 18, user.getObjectId(), user.getLastName());
            String intoAttributes5 = String.format(insertAttributeTemplate, 19, user.getObjectId(), user.getAdditionalInfo());
            String intoReferences = String.format(insertReferenceTemplate, 20, user.getObjectId(), user.getRole().getObjectId());
            //оставлю комменты чтобы не забыть по поводу автокоммитов когда/если будем переделывать
            //здесь по идее начинаем транзакцию
            jdbcTemplate.execute(intoObjects);
            jdbcTemplate.execute(intoAttributes1);
            jdbcTemplate.execute(intoAttributes2);
            jdbcTemplate.execute(intoAttributes3);
            jdbcTemplate.execute(intoAttributes4);
            jdbcTemplate.execute(intoAttributes5);
            jdbcTemplate.execute(intoReferences);
        } catch (NullPointerException e) {
            throw new TransactionException(user);
        }

        //а здесь коммитим если все окей
    }

    /**
     * Метод получает нового юзера и старого, затем сравнивает их поля и выполняет запросы по шаблону
     */
    @Override
    public void updateUser(User newUser, User oldUser) throws TransactionException {
        try {
            String updateAttributesTemplate = "UPDATE ATTRIBUTES SET VALUE = '%s' WHERE OBJECT_ID = %d AND ATTR_ID = %d";
            String updateReferenceTemplate = "UPDATE OBJREFERENCE SET REFERENCE = %d WHERE OBJECT_ID = %d AND ATTR_ID = %d";
            if (!oldUser.getEmail().equals(newUser.getEmail())) {
                jdbcTemplate.execute(String.format(updateAttributesTemplate, newUser.getEmail(), newUser.getObjectId(), 15));
            }
            if (!oldUser.getPassword().equals(newUser.getPassword())) {
                jdbcTemplate.execute(String.format(updateAttributesTemplate, newUser.getPassword(), newUser.getObjectId(), 16));
            }
            if (!oldUser.getFirstName().equals(newUser.getFirstName())) {
                jdbcTemplate.execute(String.format(updateAttributesTemplate, newUser.getFirstName(), newUser.getObjectId(), 17));
            }
            if (!oldUser.getLastName().equals(newUser.getLastName())) {
                jdbcTemplate.execute(String.format(updateAttributesTemplate, newUser.getLastName(), newUser.getObjectId(), 18));
            }
            if (!oldUser.getLastName().equals(newUser.getLastName())) {
                jdbcTemplate.execute(String.format(updateAttributesTemplate, newUser.getAdditionalInfo(), newUser.getObjectId(), 19));
            }
            if (oldUser.getRole().getObjectId() != newUser.getRole().getObjectId())
            jdbcTemplate.execute(String.format(updateReferenceTemplate, newUser.getRole().getObjectId(), newUser.getObjectId(), 20));
        } catch (NullPointerException e) {
            throw new TransactionException(newUser);
        }
    }

}
