package dev.destan.template;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html");
    }

    // https://github.com/spring-projects/spring-framework/issues/31366
    // @Override
    // public void configurePathMatch(PathMatchConfigurer configurer) {
    //     configurer.setUseTrailingSlashMatch(true);
    // }
}