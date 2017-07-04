package projectpackage.service.fileservice.files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderFileNameGenerator {
    @Value("${files.default.filename}")
    private String prefix;

    public OrderFileNameGenerator() {
    }

    public OrderFileNameGenerator(String prefix) {
        this.prefix = prefix;
    }

    public String generateFileName(String postfix, int orderId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append(postfix);
        stringBuilder.append(orderId);
        return stringBuilder.toString();
    }
}
