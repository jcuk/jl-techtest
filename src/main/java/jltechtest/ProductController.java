package jltechtest;

import org.springframework.web.bind.annotation.RestController;

import jltechtest.data.Product;
import jltechtest.formatter.PriceLabelFormatter.PriceFormat;

import org.apache.logging.log4j.Logger;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("product")
public class ProductController {
	private static final Logger LOG = LogManager.getLogger();
	private final ProductFetchController fetchController;
	
	@Autowired
	public ProductController(final ProductFetchController fetchController) {
		this.fetchController = fetchController;
	}
	
    @GetMapping(path="/discount", produces = "application/json")
    public List<Product> getDiscountedProducts() {
    	LOG.info("Getting discounted products");
    	//TODO: parameterise format / default to was now
        return fetchController.getDiscountedProducts(PriceFormat.WasNow);
    }
    
}
