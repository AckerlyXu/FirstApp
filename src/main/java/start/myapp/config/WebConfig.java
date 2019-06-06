package start.myapp.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("start.myapp.controller")

public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public ViewResolver viewResolver() {

		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/jsps/");
		resolver.setSuffix(".jsp");

		return resolver;
	}

	@Bean
	public MultipartResolver multipartResolver() throws IOException {

		return new StandardServletMultipartResolver();
	}

}
