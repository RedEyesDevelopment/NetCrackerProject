package projectpackage.repository.support.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import projectpackage.model.security.AuthCredentials;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthCredentialsRowMapper implements RowMapper<AuthCredentials> {
    @Override
    public AuthCredentials mapRow(ResultSet resultSet, int i) throws SQLException {
        AuthCredentials authCredentials = new AuthCredentials();
        authCredentials.setUserId(resultSet.getInt("USERID"));
        authCredentials.setLogin(resultSet.getString("LOGIN"));
        authCredentials.setPassword(resultSet.getString("PASSWORD"));
        authCredentials.setRolename(resultSet.getString("ROLE"));
        return authCredentials;
    }
}
