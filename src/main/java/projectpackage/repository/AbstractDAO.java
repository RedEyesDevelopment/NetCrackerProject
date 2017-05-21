package projectpackage.repository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.reacteav.ReactEAVManager;

import java.sql.Types;
import java.util.Map;

/**
 * Created by Arizel on 17.05.2017.
 */
public abstract class AbstractDAO {

    private static final Logger LOGGER = Logger.getLogger(AbstractDAO.class);

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

    public void deleteSingleEntityById(int id) throws ReferenceBreakException {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("DELETE_OBJECT_BY_ID")
                .declareParameters(
                        new SqlParameter("obj_id", Types.BIGINT),
                        new SqlOutParameter("count_rows", Types.BIGINT));

        Map<String, Object> execute = null;
        try {
            execute = call.execute(new MapSqlParameterSource("obj_id", id));
        } catch (UncategorizedSQLException e) {
            if (e.getSQLException().getErrorCode() == 20001) {
                String message = e.getSQLException().getMessage();
                message = message.substring(11);
                ReferenceBreakException rbe = new ReferenceBreakException(message.split(" "));
                LOGGER.info("Someone tried delete Entity, witch have references on self.");
                throw rbe;
            } else {
                throw e;
            }
        }
    }
}
