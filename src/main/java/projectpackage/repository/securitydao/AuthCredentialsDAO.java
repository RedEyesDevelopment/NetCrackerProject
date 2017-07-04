package projectpackage.repository.securitydao;

import projectpackage.model.security.AuthCredentials;

public interface AuthCredentialsDAO {
    public AuthCredentials getUserByUsername(String username);
}
