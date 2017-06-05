package tests.customtests;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.IgnoreHiddenFileListFilter;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import projectpackage.service.fileservice.files.DateFileNameGenerator;
import projectpackage.service.fileservice.files.OrderFileNameGenerator;

/**
 * Created by Lenovo on 05.06.2017.
 */
@Configuration
public class TestFileConfig {

    @Value("NCHotel\\w\\D{5,25}\\d{3,10}\\.(pdf)")
    private String filePattern;

    @Value("webapp.pdfs")
    private String pdfsDirectory;

    @Value("NCHotel")
    private String filePrefix;

    @Bean
    FileReadingMessageSource fileReadingMessageSource(){
        FileReadingMessageSource fileReading = new FileReadingMessageSource();
        fileReading.setFilter(compositeFileListFilter());
        return fileReading;
    }

    @Bean
    @ServiceActivator(inputChannel = "writeToFileChannel")
    FileWritingMessageHandler fileWritingMessageHandler(){
        Expression directoryExpression = new SpelExpressionParser().parseExpression(pdfsDirectory);
        FileWritingMessageHandler writingHandler = new FileWritingMessageHandler(directoryExpression);
        writingHandler.setFileExistsMode(FileExistsMode.APPEND);
        return fileWritingMessageHandler();
    }

    @Bean
    CompositeFileListFilter compositeFileListFilter(){
        CompositeFileListFilter filter = new CompositeFileListFilter();
        filter.addFilter(acceptOnceFileListFilter());
        filter.addFilter(regexPatternFileListFilter());
        filter.addFilter(ignoreHiddenFileListFilter());
        return filter;
    }

    @Bean
    AcceptOnceFileListFilter acceptOnceFileListFilter(){
        return new AcceptOnceFileListFilter();
    }

    @Bean
    IgnoreHiddenFileListFilter ignoreHiddenFileListFilter() {
        return new IgnoreHiddenFileListFilter();
    }

    @Bean
    RegexPatternFileListFilter regexPatternFileListFilter() {
        return new RegexPatternFileListFilter(filePattern);
    }

    @Bean
    OrderFileNameGenerator fileNameGenerator(){
        return new OrderFileNameGenerator(filePrefix);
    }

    @Bean
    DateFileNameGenerator dateFileNameGenerator() {
        return new DateFileNameGenerator(filePrefix);
    }

}
