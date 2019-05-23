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
import jltechtest.data.Product;
import jltechtest.formatter.RGBColourHelper;

@SpringBootTest(classes= {ProductFetchController.class, RGBColourHelper.class})
public class ProductFecthControllerTest {
	
	@Autowired
	private ProductFetchController productFectchController;
	
	@MockBean
	private ProductFetcher productFetcher;
	
	private Product product1 = new Product();
	private Product product2 = new Product();
	private Product product3 = new Product();
	private Product product4 = new Product();
	private Product product5 = new Product();
	
	@BeforeEach
	public void setUp() {
		final List<Product>products = new ArrayList<>();
				
		product1.setColorSwatches(createColourSwatchArray("Red","Azure"));
		product2.setColorSwatches(createColourSwatchArray("Green","Brown"));
		product3.setColorSwatches(createColourSwatchArray("Orange","Blue"));
		product4.setColorSwatches(createColourSwatchArray("grey"));
				
		products.add(product1);
		
		
		products.add(product2);
		products.add(product3);
		products.add(product4);
		products.add(product5);
		
		Mockito.when(productFetcher.getProducts()).thenReturn(products);
	}
	
	@Test
	public void test() {
		final List<Product> products = productFectchController.getDiscountedProducts();
		
		assertEquals("Number of products",5,products.size());
		
		final Product product1 = products.get(0);
		final Product product2 = products.get(1);
		final Product product3 = products.get(2);
		final Product product4 = products.get(3);
		final Product product5 = products.get(4);
		
		assertEquals("Red", "FF0000", product1.getColorSwatches()[0].getRgbColor());
		assertEquals("Azure", "F0FFFF", product1.getColorSwatches()[1].getRgbColor());
		
		assertEquals("Green", "008000", product2.getColorSwatches()[0].getRgbColor());
		assertEquals("Brown", "A52A2A", product2.getColorSwatches()[1].getRgbColor());
		
		assertEquals("Orange", "FFA500", product3.getColorSwatches()[0].getRgbColor());
		assertEquals("Blue", "0000FF", product3.getColorSwatches()[1].getRgbColor());
		
		assertEquals("Grey", "808080", product4.getColorSwatches()[0].getRgbColor());
		
		assertEquals("Swatch array length for no swatches", 0, product5.getColorSwatches().length);
			
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

}
