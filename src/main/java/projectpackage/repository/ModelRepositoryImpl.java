package projectpackage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import projectpackage.model.Model;
import projectpackage.support.ModelRowMapper;

import java.util.List;

/**
 * Created by Lenovo on 09.04.2017.
 */
@Repository
public class ModelRepositoryImpl implements ModelRepository {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Model getModel(int id) {
        String sqlQuery = "select IN_ID \"IN_ID\", IN_DATA \"IN_DATA\", MO_ID \"M_ID\", DATEOFREG \"M_DATE\" from MODEL M join INNER_OBJ I on M.INNER_ID = I.IN_ID where M.MO_ID = :mid";
        SqlParameterSource namedParameters = new MapSqlParameterSource("mid", id);
        RowMapperResultSetExtractor<Model> resultSetExtractor = new RowMapperResultSetExtractor<Model>(new ModelRowMapper());
        List<Model> result = namedParameterJdbcTemplate.query(sqlQuery, namedParameters, resultSetExtractor);
        if (!result.isEmpty()) {
            Model model = result.get(0);
            return model;
        } else {
            return null;
        }
    }
}
