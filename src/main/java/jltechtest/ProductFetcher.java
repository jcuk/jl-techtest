package jltechtest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
	
	public List<Product> getProducts() throws InterruptedException, ExecutionException {
		final List<Product> products = new ArrayList<>();
		
		final CompletableFuture<ProductsPage> firstProductsPage = getProductPage(null);
		
		LOG.info("Downloaded first page with {} products", firstProductsPage.get().getProducts().size());
		products.addAll(firstProductsPage.get().getProducts());
		
		final int pages = firstProductsPage.get().getPagesAvailable();
		LOG.info("Downloading {} pages of products",pages);
		
		for (int page=1;page<pages;page++) {
			LOG.info("Downloading page {} of {} of products",page, pages);
			final CompletableFuture<ProductsPage> productPage = getProductPage(page);
			LOG.info("Downloaded page {} with {} products",page, productPage.get().getProducts().size());
			products.addAll(productPage.get().getProducts());
		}
		
		return products;
	}
	
	@Async
	private CompletableFuture<ProductsPage> getProductPage(final Integer page) {
		final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint)
				.pathSegment(category, objects)
				.queryParam("key", key);
		
		if (page != null) {
			LOG.info("Getting products from {} page {}", endpoint, page);	
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
		
		LOG.info("Found {} products",products.getProducts().size());
		
		return CompletableFuture.completedFuture(products);
	}

}
