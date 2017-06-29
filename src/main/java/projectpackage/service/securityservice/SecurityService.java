package projectpackage.service.securityservice;

import projectpackage.model.auth.User;

/**
 * Created by Gvozd on 07.01.2017.
 */
public interface SecurityService {
    boolean cryptUserPass(User user);
    boolean isPasswordMatchEncrypted(String raw, String encoded);
    int getAuthenticatedUserId(String s);
    Boolean autologin(String username, String password);
}
