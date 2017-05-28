package projectpackage.repository.support.rowmappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Sergey on 28.05.2017.
 */
public class IdRowMapper implements RowMapper {
    @Override
    public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getInt("OBJECT_ID");
    }
}
