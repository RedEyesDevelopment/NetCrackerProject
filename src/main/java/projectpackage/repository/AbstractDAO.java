package projectpackage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import projectpackage.repository.reacteav.ReactEAVManager;

import java.sql.Types;
import java.util.Map;

/**
 * Created by Arizel on 17.05.2017.
 */
public abstract class AbstractDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    protected ReactEAVManager manager;

    protected final String insertObject = "INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (?, ?, ?, ?, ?)";
    protected final String insertAttribute = "INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (?, ?, ?, ?)";
    protected final String insertObjReference = "INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (?, ?, ?)";

    protected final String updateAttribute = "UPDATE ATTRIBUTES SET VALUE = ?, DATE_VALUE = ? WHERE OBJECT_ID = ? AND ATTR_ID = ?";
    protected final String updateReference = "UPDATE OBJREFERENCE SET REFERENCE = ? WHERE OBJECT_ID = ? AND ATTR_ID = ?";

    public Integer nextObjectId() {
        return jdbcTemplate.queryForObject("SELECT seq_obj_id.nextval FROM DUAL", Integer.class);
    }

    public int deleteSingleEntityById(int id) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("DELETE_OBJECT_BY_ID")
                .declareParameters(
                        new SqlParameter("obj_id", Types.BIGINT),
                        new SqlOutParameter("count_rows", Types.BIGINT));

        Map<String, Object> execute = call.execute(new MapSqlParameterSource("obj_id", id));
        return Math.toIntExact((Long) execute.get("count_rows"));
    }
}
