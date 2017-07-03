package projectpackage.service.linksservice;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.User;
import projectpackage.service.fileservice.mails.MailService;
import projectpackage.service.support.RandomStringGenerator;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Log4j
@Service
public class PasswordChangeServiceImpl implements PasswordChangeService {
    private LoadingCache<String, User> containers = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).initialCapacity(5).maximumSize(30).build(new CacheLoader<String, User>() {
        @Override
        public User load(String s) throws Exception {
            return null;
        }
    });

    @Autowired
    RandomStringGenerator randomStringGenerator;

    @Autowired
    MailService mailService;

    @Value("${server.http}")
    private String http;

    @Override
    public void createPasswordChangeTarget(User targetUser) {
        System.out.println("IN PC SERVICE");
        String newLink = null;
        try {
            newLink = newLink();
        } catch (ExecutionException e) {
            log.warn("User getting from Guava cache was failed due to unknown reason."+e);
        }
        containers.put(newLink, targetUser);
        StringBuilder builder = new StringBuilder(http).append("/cpass/").append(newLink);
        mailService.sendEmailToChangePassword(targetUser.getEmail(), 4, builder.toString());
    }

    private String newLink() throws ExecutionException {
        String newLink = null;
        Random random = new Random();
        while (true) {
            newLink = randomStringGenerator.nextString(20);
            if (null != containers.get(newLink)) {
                break;
            }
        }
        return newLink;
    }

    @Override
    public boolean verifyDynamicLink(String link) {
        User user = null;
        try {
            user = containers.get(link);
        } catch (ExecutionException e) {
            log.warn("User getting from Guava cache was failed due to unknown reason."+e);
        }
        if (null == user) {
            return false;
        }
        String newPassword = randomStringGenerator.nextString(10);
        mailService.sendEmailToChangePassword(user.getEmail(), 5, newPassword);
        return true;
    }

}
