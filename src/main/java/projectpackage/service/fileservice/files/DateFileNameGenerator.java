package projectpackage.service.fileservice.files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateFileNameGenerator {
    @Value("${files.default.filename}")
    private String prefix;

    private DateFormat targetDateFormat = new SimpleDateFormat("MM-dd-yyyy");
    private DateFormat currentTimeFormat = new SimpleDateFormat("HH-mm-ss_MM-dd-yyyy");

    public DateFileNameGenerator() {
    }

    public DateFileNameGenerator(String prefix) {
        this.prefix = prefix;
    }

    public String generateFileName(String postfix, Date date){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append(postfix);
        stringBuilder.append(targetDateFormat.format(date));
        return stringBuilder.toString();
    }

    public String generateFileNameWithCurrentTime(String postfix){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append(postfix);
        stringBuilder.append(currentTimeFormat.format(System.currentTimeMillis()));
        return stringBuilder.toString();
    }
}
