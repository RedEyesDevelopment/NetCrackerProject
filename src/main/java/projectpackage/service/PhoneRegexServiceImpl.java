package projectpackage.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by Lenovo on 02.05.2017.
 */
@Service
public class PhoneRegexServiceImpl implements PhoneRegexService {
    private static final String[] REGEXES = {"\\+\\d{1,2}\\(\\d{3}\\)\\d{2}\\-\\d{2}\\-\\d{3}", "\\+\\d{12}", "\\d{7}", "[0]\\d{9}"};

    @Override
    public boolean match(String phonenum) {
        return Arrays.stream(REGEXES).anyMatch(phonenum::matches);
    }
}
