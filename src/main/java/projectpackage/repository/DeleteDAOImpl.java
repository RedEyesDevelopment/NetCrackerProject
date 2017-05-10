package projectpackage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by Arizel on 09.05.2017.
 */
@Repository
public class DeleteDAOImpl implements DeleteDAO{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int deleteSingleEntityById(int id) {
        jdbcTemplate.execute("DELETE FROM OBJECTS WHERE OBJECTS.OBJECT_ID="+id);
        return 0;
    }

//    @Override
//    public int deleteSingleEntityById(int id) {
//        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
//                .withFunctionName("DELETE_OBJECT_BY_ID")
//                .declareParameters(
//                        new SqlParameter("obj_id", Types.BIGINT),
//                        new SqlOutParameter("count_rows", Types.BIGINT));
//
//        Map<String, Object> execute = call.execute(new MapSqlParameterSource("obj_id", id));
//        jdbcTemplate.execute("Rollback");
//        return Math.toIntExact((Long) execute.get("count_rows"));
//    }
}
