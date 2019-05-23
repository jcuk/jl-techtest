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
	
	public List<Product> getDiscountedProducts(final PriceFormat priceFormat) {
		final List<Product> products = productFetcher.getProducts();

		LOG.info("Retrieved {} products from api",products.size());
		
		return productFetcher.getProducts().stream()
				.filter(p -> p.hasPriceReduction())
				.sorted((p1,p2) -> p1.comparePriceReduction(p2))
				.map(p -> enrichLabel(p, priceFormat))
				.collect(Collectors.toList());

	}
	
	/** Enriches the data on each product. This consists of:
	 * <ul>
	 * <li>Looking up the RGB colour</li>
	 * <li>Populating the now price</li>
	 * <li>populating the price label</li>
	 * </ul>
	 * @param product
	 * @param PriceFormat for the price label
	 */
	private Product enrichLabel(final Product product, final PriceFormat format) {
		//Populate the RGB information on the swatches
		for (final ColorSwatch swatch : product.getColorSwatches()) {
			swatch.setRgbColor(rgbColourHelper.colourToRGB(swatch.getBasicColor()));
		}
		
		//Populate the 'nowPrice' if price is present
		final Price price = product.getPrice();
		if (price != null) {
			product.setNowPrice(PriceLabelFormatter.formatCurrencyIgnoreErrors(price.getNow(), price.getCurrency()));
			
			//Populate the 'priceLabel' according to the selected format
			product.setPriceLabel(PriceLabelFormatter.format(price, format));
		}
		
		return product;
	}
}
