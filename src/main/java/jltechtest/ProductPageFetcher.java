package jltechtest;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import jltechtest.data.ProductsPage;

/** Service to retrieve a single Product page from the API asynchronously
 * 
 * @author jason
 *
 */
@Service
@PropertySource("classpath:configuration.properties")
public class ProductPageFetcher {
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

	@Async
	public CompletableFuture<ProductsPage> getProductPage(final Integer page) {
		final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint)
				.pathSegment(category, objects)
				.queryParam("key", key);
		
		if (page != null) {
			LOG.info("Getting products from page {}", page);	
			builder.queryParam("page", page);
		} else {
			LOG.info("Getting products from {}", endpoint);			
		}
		
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		final HttpEntity<String> entity = new HttpEntity<>("body", headers);
		
		final ResponseEntity<ProductsPage> response =
			restTemplate.exchange(builder.build().toUri(),
			    HttpMethod.GET,
			    entity,
			    ProductsPage.class

			);
		
		final ProductsPage products = response.getBody();
		products.setPageId(page);
		
		if (page != null) {
			LOG.info("Found {} products on page {}",products.getProducts().size(), page);
		} else {
			LOG.info("Found {} products on first page",products.getProducts().size());
		}
		
		return CompletableFuture.completedFuture(products);
	}
}
