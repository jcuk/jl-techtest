package jltechtest;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import jltechtest.data.parsing.TollerantObjectMapper;

@Configuration
@EnableConfigurationProperties
@EnableAsync
@PropertySource("classpath:configuration.properties")
public class AppConfig {
	@Value("${executor.poolSize}")
	private int poolSize;
	
	@Value("${executor.maxPoolSize}")
	private int maxPoolSize;
	
	@Value("${executor.queueCapacity}")
	private int queueCapacity;
	
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
	
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }

}
