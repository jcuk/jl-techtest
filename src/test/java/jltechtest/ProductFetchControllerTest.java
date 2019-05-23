package jltechtest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import jltechtest.data.ColorSwatch;
import jltechtest.data.Price;
import jltechtest.data.Product;
import jltechtest.formatter.RGBColourHelper;
import jltechtest.formatter.PriceLabelFormatter.PriceFormat;

@SpringBootTest(classes= {ProductFetchController.class, RGBColourHelper.class})
public class ProductFetchControllerTest {
	
	@Autowired
	private ProductFetchController productFectchController;
	
	@MockBean
	private ProductFetcher productFetcher;
	
	private Product product1 = new Product();
	private Product product2 = new Product();
	private Product product3 = new Product();
	private Product product4 = new Product();
	private Product product5 = new Product();
	private Product product6 = new Product();
	private Product product7 = new Product();
	
	@BeforeEach
	public void setUp() {
		final List<Product>products = new ArrayList<>();
				
		product1.setColorSwatches(createColourSwatchArray("Red","Azure"));
		product2.setColorSwatches(createColourSwatchArray("Green","Brown"));
		product3.setColorSwatches(null);
		product4.setColorSwatches(createColourSwatchArray("grey"));
		product5.setColorSwatches(createColourSwatchArray("Orange","Blue"));
		product6.setColorSwatches(createColourSwatchArray("Yellow","Grey"));
		product7.setColorSwatches(createColourSwatchArray("black"));
		
		product1.setPrice(makePrice("1.99", "5.99", "GBP"));					//03.00 reduction
		product2.setPrice(makePrice("10.00","20.50","GBP"));					//10.50 reduction
		product3.setPrice(makePrice("50.00", "100.00", "EUR","90.00"));			//50.00 reduction
		product4.setPrice(makePrice("50.50", "100.00", "GBP","90.00","85.50")); //49.50 reduction
		product5.setPrice(makePrice("invalid","","","","GBP"));
		product6.setPrice(makePrice("1.99", "", "GBP")); // No was price - filter out
		product7.setPrice(makePrice("fifty", "forty", "GBP")); // Invalid price
		
				
		products.add(product1);
		products.add(product2);
		products.add(product3);
		products.add(product4);
		products.add(product5);
		products.add(product6);
		products.add(product7);
		
		Mockito.when(productFetcher.getProducts()).thenReturn(products);
	}
	
	@Test
	public void testRgbLookup() {
		final List<Product> products = productFectchController.getDiscountedProducts(PriceFormat.ShowWasNow);
		
		assertEquals("Number of products",5,products.size());
		
		final Product product1 = products.get(0);
		final Product product2 = products.get(1);
		final Product product3 = products.get(2);
		final Product product4 = products.get(3);
		final Product product5 = products.get(4);
		
		assertEquals("Swatch array length for no swatches", 0, product1.getColorSwatches().length);
		
		assertEquals("Grey", "808080", product2.getColorSwatches()[0].getRgbColor());
		
		assertEquals("Green", "008000", product3.getColorSwatches()[0].getRgbColor());
		assertEquals("Brown", "A52A2A", product3.getColorSwatches()[1].getRgbColor());
		
		assertEquals("Red", "FF0000", product4.getColorSwatches()[0].getRgbColor());
		assertEquals("Azure", "F0FFFF", product4.getColorSwatches()[1].getRgbColor());
		
		assertEquals("Black", "000000", product5.getColorSwatches()[0].getRgbColor());
	}
	
	@Test
	public void testNowPrice() {
		final List<Product> products = productFectchController.getDiscountedProducts(PriceFormat.ShowWasNow);
		
		assertEquals("Number of products",5,products.size());
		
		final Product product1 = products.get(0);
		final Product product2 = products.get(1);
		final Product product3 = products.get(2);
		final Product product4 = products.get(3);
		final Product product5 = products.get(4);
		
		//Sorted into reduction (not price) order
		assertEquals("Now price", "€50", product1.getNowPrice()); //3
		assertEquals("Now price", "£50.50", product2.getNowPrice()); //4
		assertEquals("Now price", "£10", product3.getNowPrice()); //2
		assertEquals("Now price", "£1.99", product4.getNowPrice()); //1
		
		//Invalid price reduction - last
		assertEquals("Now price for not a valid price", "", product5.getNowPrice());
	}
	
	@Test
	public void testWasNowPriceLabel() {
		final List<Product> products = productFectchController.getDiscountedProducts(PriceFormat.ShowWasNow);
		
		assertEquals("Number of products",5,products.size());
		
		final Product product1 = products.get(0);
		final Product product2 = products.get(1);
		final Product product3 = products.get(2);
		final Product product4 = products.get(3);
		final Product product5 = products.get(4);
		
		assertEquals("Was now price", "Was €100, now €50", product1.getPriceLabel());
		assertEquals("Was now price", "Was £100, now £50.50", product2.getPriceLabel());
		assertEquals("Was now price", "Was £20.50, now £10", product3.getPriceLabel());
		assertEquals("Was now price", "Was £5.99, now £1.99", product4.getPriceLabel());
		assertEquals("Label for not a valid price", "", product5.getPriceLabel());
	}
	
	@Test
	public void testWasThenNowPriceLabel() {
		final List<Product> products = productFectchController.getDiscountedProducts(PriceFormat.ShowWasThenNow);
		
		assertEquals("Number of products",5,products.size());
		
		final Product product1 = products.get(0);
		final Product product2 = products.get(1);
		final Product product3 = products.get(2);
		final Product product4 = products.get(3);
		final Product product5 = products.get(4);
		
		assertEquals("Was now price", "Was €100, then €90, now €50", product1.getPriceLabel());
		assertEquals("Was now price", "Was £100, then £85.50, now £50.50", product2.getPriceLabel());
		assertEquals("Was now price", "Was £20.50, now £10", product3.getPriceLabel());
		assertEquals("Was now price", "Was £5.99, now £1.99", product4.getPriceLabel());
		assertEquals("Label for not a valid price", "", product5.getPriceLabel());
	}
	
	@Test
	public void testShowPercPiscountPriceLabel() {
		final List<Product> products = productFectchController.getDiscountedProducts(PriceFormat.ShowPercDscount);
		
		assertEquals("Number of products",5,products.size());
		
		final Product product1 = products.get(0);
		final Product product2 = products.get(1);
		final Product product3 = products.get(2);
		final Product product4 = products.get(3);
		final Product product5 = products.get(4);
		
		assertEquals("Was now price", "50% off - now €50", product1.getPriceLabel());
		assertEquals("Was now price", "49% off - now £50.50", product2.getPriceLabel());
		assertEquals("Was now price", "51% off - now £10", product3.getPriceLabel());
		assertEquals("Was now price", "66% off - now £1.99", product4.getPriceLabel());
		assertEquals("Label for not a valid price", "", product5.getPriceLabel());
	}
	
	private ColorSwatch[] createColourSwatchArray(final String... colours) {
		final List<ColorSwatch> colourswatches = new ArrayList<>();
		for (final String colour : colours) {
			final ColorSwatch cs = new ColorSwatch();
			cs.setBasicColor(colour);
			colourswatches.add(cs);
		}
		
		return colourswatches.toArray(new ColorSwatch[]{});
	}
	
	private Price makePrice(final String now, final String was, final String currency, final String then1, final String then2) {
		final Price price = new Price();
		price.setNow(now);
		price.setCurrency(currency);
		price.setThen1(then1);
		price.setThen2(then2);
		price.setWas(was);
		return price;
	}
	
	private Price makePrice(final String now, final String was, final String currency, final String then1) {
		return makePrice(now, was, currency, then1, null);
	}
	
	private Price makePrice(final String now, final String was, final String currency) {
		return makePrice(now, was, currency, null, null);
	}

}
