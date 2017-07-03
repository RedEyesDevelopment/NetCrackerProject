package projectpackage.service.linksservice;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.User;
import projectpackage.service.authservice.UserService;
import projectpackage.service.fileservice.mails.MailService;
import projectpackage.service.support.RandomStringGenerator;

import java.util.concurrent.TimeUnit;

@Log4j
@Service
public class PasswordChangeServiceImpl implements PasswordChangeService {
    private LoadingCache<String, String> containers = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).initialCapacity(5).maximumSize(30).build(new CacheLoader<String, String>() {
        @Override
        public String load(String s) throws Exception {
            return new String("");
        }
    });

    @Autowired
    RandomStringGenerator randomStringGenerator;

    @Autowired
    MailService mailService;

    @Autowired
    UserService userService;

    @Value("${server.http}")
    private String http;

    @Override
    public void createPasswordChangeTarget(User targetUser) {
        String newLink = newLink();
        containers.put(newLink, targetUser.getEmail());
        StringBuilder builder = new StringBuilder(http).append("/cpass/").append(newLink);
        mailService.sendEmailToChangePassword(targetUser.getEmail(), 4, builder.toString());
    }

    private String newLink() {
        String newLink = null;
        String temp = null;
        while (true) {
            newLink = randomStringGenerator.nextString(20);
                temp = containers.getUnchecked(newLink);
            if (null==temp || temp.isEmpty()) {
                break;
            }
        }
        return newLink;
    }

    @Override
    public boolean verifyDynamicLink(String link) {
        String userLogin = null;
            userLogin = containers.getUnchecked(link);
        if (null == userLogin || userLogin.isEmpty()) {
            return false;
        }
        User user = userService.getSingleUserByUsername(userLogin);
        if (null!=user){
            containers.invalidate(link);
            String newPassword = randomStringGenerator.nextString(10);
            user.setPassword(newPassword);
            userService.updateUserPassword(user.getObjectId(), user);
            mailService.sendEmailToChangePassword(userLogin, 5, newPassword);
            return true;
        }
        return false;
    }

}
