package projectpackage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Phone;

@Repository
public class PhoneDAOImpl implements PhoneDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;



    @Override
    public void insertPhone(Phone phone) {
        String intoObjects = String.format("INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) " +
                "VALUES (%d, %d, 9, '%s', NULL)", phone.getObjectId(), phone.getUserId(), phone.getPhoneNumber());
        String intoAttributes = String.format("INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) " +
                "VALUES (38,%d,'08001234516',NULL)", phone.getObjectId());

        jdbcTemplate.execute(intoObjects);
        jdbcTemplate.execute(intoAttributes);
    }

    @Override
    public void updatePhone(Phone newPhone, Phone oldPhone) {
        String queryTemplate = "UPDATE ATTRIBUTES SET VALUE = '%s' WHERE OBJECT_ID = %d AND ATTR_ID = %d";
        if (!oldPhone.getPhoneNumber().equals(newPhone.getPhoneNumber())) {
            jdbcTemplate.execute(String.format(queryTemplate, newPhone.getPhoneNumber(), newPhone.getObjectId(), 38));
        }
    }
}
