package projectpackage.service.linksservice;

import projectpackage.model.auth.User;

public interface PasswordChangeService {
    void createPasswordChangeTarget(User targetUser);
    boolean verifyDynamicLink(String link);
}
