package projectpackage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Phone;

import java.util.List;

@Repository
public class PhoneDAOImpl implements PhoneDAO {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
}
