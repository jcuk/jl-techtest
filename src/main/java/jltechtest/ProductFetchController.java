package jltechtest;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import jltechtest.data.Product;
import jltechtest.data.ProductsPage;
import jltechtest.data.RGBColourHelper;

@Controller
public class ProductFetchController {
	private static final Logger LOG = LogManager.getLogger();
	
	private final String endpoint="https://jl-nonprod-syst.apigee.net/v1/categories";
	private final String category = "600001506";
	private final String objects = "products";
	private final String key="2ALHCAAs6ikGRBoy6eTHA58RaG097Fma";
	
	@Autowired
	private RestOperations restTemplate;
	
	@Autowired
	private RGBColourHelper grbColourHelper;
	
	public List<Product> getDiscountedProducts() {
		//TODO: Aggregate all pages
		//TODO: Filter non discount items
		//TODO: enrich labels
		//TODO: sort by highest price reduction

		final List<Product> products = new ArrayList<>();
		
		grbColourHelper.colourToRGB("green");
		
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
