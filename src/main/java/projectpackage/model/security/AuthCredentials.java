package projectpackage.model.security;

import lombok.Data;

@Data
public class AuthCredentials {
    private int userId;
    private String login;
    private String password;
    private String rolename;
}
