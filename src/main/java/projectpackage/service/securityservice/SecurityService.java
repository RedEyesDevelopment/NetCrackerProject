package projectpackage.service.securityservice;

import projectpackage.model.auth.User;

/**
 * Created by Gvozd on 07.01.2017.
 */
public interface SecurityService {
    boolean cryptUserPass(User user);
    int getAuthenticatedUserId(String s);
    Boolean autologin(String username, String password);
}
