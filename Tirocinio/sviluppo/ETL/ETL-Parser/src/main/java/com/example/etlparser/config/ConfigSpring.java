package com.example.etlparser.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration
@EnableJpaRepositories("com.example.etlparser")
public class ConfigSpring implements WebMvcConfigurer  {

	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/swagger-ui.html");
	    registry.addRedirectViewController("/swagger-ui.html", "/swagger-ui/");
    }

	@Bean
    public InternalResourceViewResolver defaultViewResolver() {
	    return new InternalResourceViewResolver();
	}
	
}