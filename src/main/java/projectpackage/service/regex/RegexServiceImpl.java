package projectpackage.service.regex;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegexServiceImpl implements RegexService {
    private static final String[] REGEXES = {"\\+\\d{1,2}\\(\\d{3}\\)\\d{2}\\-\\d{2}\\-\\d{3}", "\\+\\d{12}", "\\d{7}", "[0]\\d{9}"};
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean isValidPhone(String phoneNumber) {
        return Arrays.stream(REGEXES).anyMatch(phoneNumber::matches);
    }
}
