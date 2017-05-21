package projectpackage.repository.securitydao;

import projectpackage.model.security.AuthCredentials;

/**
 * Created by Lenovo on 21.05.2017.
 */
public interface AuthCredentialsDAO {
    public AuthCredentials getUserByUsername(String username);
}
