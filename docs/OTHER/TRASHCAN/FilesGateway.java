package projectpackage.service.fileservice.files;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.File;

/**
 * Created by Lenovo on 05.06.2017.
 */
@MessagingGateway(defaultRequestChannel = "writeToFileChannel")
public interface FilesGateway {
    void writeToFile(@Header(FileHeaders.FILENAME) String fileName, @Header(FileHeaders.FILENAME) File directory, String data);
}
