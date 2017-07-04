package projectpackage.service.securityservice;

import projectpackage.model.auth.User;

public interface SecurityService {
    boolean cryptUserPass(User user);
    boolean isPasswordMatchEncrypted(String raw, String encoded);
    int getAuthenticatedUserId(String s);
    Boolean autologin(String username, String password);
}
