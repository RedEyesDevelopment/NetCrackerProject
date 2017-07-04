package projectpackage.repository.securitydao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.security.AuthCredentials;
import projectpackage.repository.support.rowmappers.AuthCredentialsRowMapper;

import java.util.List;

@Repository
public class AuthCredentialsDAOImpl implements AuthCredentialsDAO {
    private static final String GETCREDENTIALS = "SELECT USR.OBJECT_ID \"USERID\", USALOG.VALUE \"LOGIN\", USAPASS.VALUE \"PASSWORD\", RLENAME.VALUE \"ROLE\"\n" +
            "FROM OBJECTS USR, ATTRTYPE USTOROLE, OBJREFERENCE USTOROLEREF, OBJTYPE ROLETYPE, OBJECTS RLE, ATTRIBUTES USALOG, ATTRIBUTES USAPASS, ATTRIBUTES RLENAME\n" +
            "WHERE USR.OBJECT_TYPE_ID = 3 AND\n" +
            "ROLETYPE.OBJECT_TYPE_ID=10 AND\n" +
            "USALOG.ATTR_ID=15 AND\n" +
            "USAPASS.ATTR_ID=16 AND\n" +
            "RLENAME.ATTR_ID=39 AND\n" +
            "USALOG.OBJECT_ID = USR.OBJECT_ID AND\n" +
            "USAPASS.OBJECT_ID = USR.OBJECT_ID AND\n" +
            "RLENAME.OBJECT_ID = RLE.OBJECT_ID AND\n" +
            "USR.OBJECT_TYPE_ID = USTOROLE.OBJECT_TYPE_ID AND\n" +
            "USTOROLE.OBJECT_TYPE_ID_REF = ROLETYPE.OBJECT_TYPE_ID AND\n" +
            "USR.OBJECT_ID=USTOROLEREF.OBJECT_ID AND\n" +
            "USTOROLE.ATTR_ID = USTOROLEREF.ATTR_ID AND\n" +
            "RLE.OBJECT_ID =USTOROLEREF.REFERENCE AND\n" +
            "USALOG.VALUE= :login";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public AuthCredentials getUserByUsername(String username) {
        AuthCredentials credentials = null;
        if (null != username && !username.equals("")) {
            SqlParameterSource namedParameters = new MapSqlParameterSource("login", username);
            RowMapperResultSetExtractor<AuthCredentials> resultSetExtractor = new RowMapperResultSetExtractor<>(new AuthCredentialsRowMapper());
            List<AuthCredentials> credentialsList = namedParameterJdbcTemplate.query(GETCREDENTIALS, namedParameters, resultSetExtractor);
            if (!credentialsList.isEmpty() && credentialsList.size() == 1) {
                credentials = credentialsList.get(0);
            }
        }
        return credentials;
    }
}
