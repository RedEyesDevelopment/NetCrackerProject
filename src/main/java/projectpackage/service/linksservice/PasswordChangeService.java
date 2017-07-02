package projectpackage.service.linksservice;

import projectpackage.model.auth.User;

/**
 * Created by Lenovo on 02.07.2017.
 */
public interface PasswordChangeService {
    void createPasswordChangeTarget(User targetUser);
    boolean verifyDynamicLink(String link);
}
