package jltechtest;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jltechtest.data.ColorSwatch;
import jltechtest.data.Price;
import jltechtest.data.Product;
import jltechtest.formatter.PriceLabelFormatter;
import jltechtest.formatter.PriceLabelFormatter.PriceFormat;
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
		//TODO: sort by highest price reduction
		
		LOG.info("Retrieved {} products from api",products.size());
		
		return productFetcher.getProducts().stream()
				.map((p) -> {
					this.enrichLabel(p);
					return p;
				})
				.collect(Collectors.toList());

	}
	
	/** Enriches the data on each product. This consists of:
	 * <ul>
	 * <li>Looking up the RGB colour</li>
	 * <li>Populating the now price</li>
	 * <li>populating the price label (TBD)</li>
	 * </ul>
	 * @param product
	 */
	private void enrichLabel(final Product product) {
		//Populate the RGB information on the swatches
		for (final ColorSwatch swatch : product.getColorSwatches()) {
			swatch.setRgbColor(rgbColourHelper.colourToRGB(swatch.getBasicColor()));
		}
		
		//Populate the 'nowPrice' if price is present
		final Price price = product.getPrice();
		if (price != null) {
			product.setNowPrice(PriceLabelFormatter.formatCurrencyIgnoreErrors(price.getNow(), price.getCurrency()));
			
			//Populate the 'priceLabel' according to the selected format
			//TODO: select format
			product.setPriceLabel(PriceLabelFormatter.format(price, PriceFormat.WasNow));
		}
		

		
	}

}
