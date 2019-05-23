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

		//TODO: sort by highest price reduction
		
		LOG.info("Retrieved {} products from api",products.size());
		
		return productFetcher.getProducts().stream()
				.filter(p -> hasPriceReduction(p))
				// Fill in labels and colours on products
				.map((p) -> enrichLabel(p, priceFormat))
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
	
	/** Return true if the product has a price reduction i.e. if both the was and the
	 * now field are present on the price
	 * @param product
	 * @return
	 */
	private boolean hasPriceReduction(final Product product) {
		return 	product.getPrice() != null &&
				product.getPrice().getWas() != null &&
				product.getPrice().getNow() != null &&
				!product.getPrice().getNow().isEmpty() &&
				!product.getPrice().getWas().isEmpty();

	}
}
