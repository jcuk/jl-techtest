package jltechtest;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import jltechtest.data.Product;
import jltechtest.data.ProductsPage;

@Service
@PropertySource("classpath:configuration.properties")
public class ProductFetcher {
	private static final Logger LOG = LogManager.getLogger();
	
	@Value("${endpoint}")
	private String endpoint;
	
	@Value("${category}")
	private String category;
	
	@Value("${objects}")
	private String objects;
	
	@Value("${key}")
	private String key;
	
	@Autowired
	private RestOperations restTemplate;
	
	public List<Product> getProducts() {
		final List<Product> products = new ArrayList<>();
		
		final ProductsPage productsPage = getfirstPage();
		
		products.addAll(productsPage.getProducts());
		
		return products;
	}
	
	private ProductsPage getfirstPage() {		
		final UriComponents builder = UriComponentsBuilder.fromUriString(endpoint)
				.pathSegment(category, objects)
				.queryParam("key", key)
	            .build();
		
		LOG.info("Getting products from {}", builder.toString());
		
		final ResponseEntity<ProductsPage> response = restTemplate.exchange(builder.toUri(),
			    HttpMethod.GET,
			    null, //Headers etc
			    ProductsPage.class

			);
		
		final ProductsPage products = response.getBody();
		
		LOG.info("Found {} products",products.getProducts().size());
		
		return products;
	}

}
