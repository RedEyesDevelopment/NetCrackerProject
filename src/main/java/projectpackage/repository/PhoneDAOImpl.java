package projectpackage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Phone;
import projectpackage.repository.reacdao.ReactEAVManager;
import projectpackage.repository.reacdao.exceptions.ResultEntityNullException;

import java.util.List;

@Repository
public class PhoneDAOImpl implements PhoneDAO {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ReactEAVManager manager;

    @Override
    public List<Phone> getPhonesList() {
        String sql = "SELECT PHONESUSER.PARENT_ID \"userId\", PHONES.VALUE \"phoneNumber\"\n" +
                "FROM ATTRIBUTES PHONES, OBJECTS PHONESUSER\n" +
                "WHERE PHONES.ATTR_ID=38 AND\n" +
                "PHONESUSER.OBJECT_TYPE_ID=9 AND\n" +
                "PHONESUSER.OBJECT_ID = PHONES.OBJECT_ID";
        List<Phone> list = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<Phone>(Phone.class));
        return list;
    }

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
    public void updatePhone(Phone phone) {
        Phone oldPhone = getOldPhone(phone.getObjectId());
        String queryTemplate = "UPDATE ATTRIBUTES SET VALUE = '%s' WHERE OBJECT_ID = %d AND ATTR_ID = %d";
        if (!oldPhone.getPhoneNumber().equals(phone.getPhoneNumber())) {
            jdbcTemplate.execute(String.format(queryTemplate, phone.getPhoneNumber(), phone.getObjectId(), 38));
        }
    }


    private Phone getOldPhone(int objectId) {
        Phone oldPhone = null;
        try {
            oldPhone = (Phone) manager.createReactEAV(Phone.class).getSingleEntityWithId(objectId);
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }

        return oldPhone;
    }
}
