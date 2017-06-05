package tests.customtests;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import projectpackage.service.fileservice.files.FilesGateway;
import projectpackage.service.fileservice.files.OrderFileNameGenerator;

/**
 * Created by Lenovo on 05.06.2017.
 */
public class CustomTests extends AbstractFileTest {

    @Autowired
    FileReadingMessageSource fileReadingMessageSource;

    @Autowired
    FileWritingMessageHandler fileWritingMessageHandler;

    @Autowired
    FilesGateway filesGateway;

    @Autowired
    OrderFileNameGenerator orderFileNameGenerator;

    @Test
    public void getApplicationContext(){
        String fileName = orderFileNameGenerator.generateFileName("OrderPayment", 111);
//        fileWritingMessageHandler.
//        filesGateway.writeToFile("foo.txt", new File(tmpDir.get));
    }
}
