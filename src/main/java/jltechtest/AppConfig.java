package jltechtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties
public class AppConfig {
	
	@Autowired
	private TollerantObjectMapper tom;
	
	@Bean
	public RestOperations restOperations() {
	    RestTemplate rest = new RestTemplate();
	    rest.getMessageConverters().add(0, mappingJacksonHttpMessageConverter());
	    return rest;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
	    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	    converter.setObjectMapper(tom.getMapper());
	    return converter;
	}

}
