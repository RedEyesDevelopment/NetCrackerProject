package projectpackage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Arizel on 17.05.2017.
 */
public class AbstractDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    protected final String insertObjects = "INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (?, ?, ?, ?, ?)";
    protected final String insertAttributes = "INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (?, ?, ?, ?)";
    protected final String insertObjReference = "INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (?, ?, ?)";

    protected final String updateAttributes = "UPDATE ATTRIBUTES SET VALUE = ?, DATE_VALUE = ? WHERE OBJECT_ID = ? AND ATTR_ID = ?";
    protected final String updateReference = "UPDATE OBJREFERENCE SET REFERENCE = ? WHERE OBJECT_ID = ? AND ATTR_ID = ?";

    public Integer nextObjectId() {
        return jdbcTemplate.queryForObject("SELECT seq_obj_id.nextval FROM DUAL", Integer.class);
    }
}
