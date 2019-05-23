package jltechtest;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jltechtest.data.ColorSwatch;
import jltechtest.data.Product;
import jltechtest.formatter.RGBColourHelper;

@Controller
public class ProductFetchController {
	private static final Logger LOG = LogManager.getLogger();
	
	@Autowired
	private RGBColourHelper rgbColourHelper;
	
	@Autowired
	private ProductFetcher productFetcher;
	
	public List<Product> getDiscountedProducts() {
		final List<Product> products = productFetcher.getProducts();

		//TODO: Filter non discount items
		//TODO: enrich labels
		//TODO: sort by highest price reduction
		
		LOG.info("Retrieved {} products from api",products.size());
		
		return productFetcher.getProducts().stream()
				.map((p) -> {
					this.enrichLabel(p);
					return p;
				})
				.collect(Collectors.toList());

	}
	
	private void enrichLabel(final Product product) {
		for (final ColorSwatch swatch : product.getColorSwatches()) {
			swatch.setRgbColor(rgbColourHelper.colourToRGB(swatch.getBasicColor()));
		}
	}

}
