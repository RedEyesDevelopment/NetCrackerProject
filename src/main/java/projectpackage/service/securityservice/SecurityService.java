package projectpackage.service.securityservice;

/**
 * Created by Gvozd on 07.01.2017.
 */
public interface SecurityService {
    int getAuthenticatedUserId(String s);
    Boolean autologin(String username, String password);
}
