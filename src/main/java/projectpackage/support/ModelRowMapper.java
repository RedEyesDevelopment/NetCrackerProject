package projectpackage.support;

import org.springframework.jdbc.core.RowMapper;
import projectpackage.model.InnerObject;
import projectpackage.model.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Lenovo on 09.04.2017.
 */
public class ModelRowMapper implements RowMapper<Model> {
    @Override
    public Model mapRow(ResultSet resultSet, int i) throws SQLException {
        InnerObject innerObject = new InnerObject();
        innerObject.setInnerId(resultSet.getInt("IN_ID"));
        innerObject.setData(resultSet.getString("IN_DATA"));
        Model model = new Model();
        model.setId(resultSet.getInt("M_ID"));
        model.setDateOfCreation(resultSet.getDate("M_DATE"));
        model.setInnerObject(innerObject);
        return model;
    }
}
