package tests.pdf;

import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import projectpackage.service.pdf.PdfService;
import projectpackage.service.pdf.PdfServiceImpl;

/**
 * Created by Gvozd on 06.01.2017.
 */
@ContextConfiguration
public class TestPDFConfig {
    @Bean
    PdfService pdfService() {
        return new PdfServiceImpl();
    }
}
