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
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for retrieving discounted products in JSON
 * @author jason
 *
 */
@RestController
@RequestMapping("product")
public class ProductController {
	private static final Logger LOG = LogManager.getLogger();
	
	@Autowired
	private ProductFetchController fetchController;

    @GetMapping(path="/discount", produces = "application/json")
    public List<Product> getDiscountedProducts(@RequestParam(defaultValue="ShowWasNow") final String labelType) {
    	PriceFormat priceFormat = PriceFormat.ShowWasNow;
    	try {
    		priceFormat = PriceFormat.valueOf(labelType);
    	} catch (IllegalArgumentException | NullPointerException e) {
    		LOG.warn("Unknown value for labelType parameter: {}",labelType);
    	}
    	LOG.info("Getting discounted products with pricing label {}",priceFormat);
        return fetchController.getDiscountedProducts(priceFormat);
    }
    
}
