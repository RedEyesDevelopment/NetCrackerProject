package projectpackage.configuration;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import projectpackage.service.support.RandomStringGenerator;
import projectpackage.support.TestInterceptor;

@Log4j
@Configuration
@EnableWebMvc
public class WebMVCConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webapp/res/**").addResourceLocations("/webapp/res/");
        registry.addResourceHandler("/webapp/views/**").addResourceLocations("/webapp/views/");
        registry.addResourceHandler("/webapp/pdfs/**").addResourceLocations("/webapp/pdfs/");
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean disableSpringBootErrorFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(errorPageFilter());
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    @Bean
    RandomStringGenerator randomStringGenerator(){
        return new RandomStringGenerator();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(testInterceptor());
    }

    @Bean
    TestInterceptor testInterceptor(){
        return new TestInterceptor();
    }
}