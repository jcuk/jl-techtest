package jltechtest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jltechtest.data.Product;
import jltechtest.data.RGBColourHelper;

@Controller
public class ProductFetchController {
	private static final Logger LOG = LogManager.getLogger();
	
	@Autowired
	private RGBColourHelper grbColourHelper;
	
	@Autowired
	private ProductFetcher productFetcher;
	
	public List<Product> getDiscountedProducts() {
		final List<Product> products = productFetcher.getProducts();

		//TODO: Aggregate all pages
		//TODO: Filter non discount items
		//TODO: enrich labels
		//TODO: sort by highest price reduction
		
		LOG.info("Retrieved {} products from api",products.size());
				
		return products;
	}

}
