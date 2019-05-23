package data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jltechtest.data.Price;
import jltechtest.data.Product;

public class ProductTest {
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private Product noPriceReduction1 = new Product();
	private Product noPriceReduction2 = new Product();
	private Product priceReduction10 = new Product();
	private Product priceReduction15 = new Product();
	private Product priceReduction15again = new Product();
	private Product priceReduction1p5 = new Product();
	
	@BeforeEach
	public void setUp() {
		noPriceReduction1.setPrice(new Price());
		noPriceReduction2.setPrice(new Price());
		
		final Price price10 = new Price();
		price10.setNow("25.50");
		price10.setWas("35.50");
		priceReduction10.setPrice(price10);
		
		final Price price15 = new Price();
		price15.setNow("2.00");
		price15.setWas("17.00");
		priceReduction15.setPrice(price15);
		
		final Price price15again = new Price();
		price15again.setNow("12.00");
		price15again.setWas("27.00");
		priceReduction15again.setPrice(price15again);
		
		final Price price1p5 = new Price();
		price1p5.setNow("25.50");
		price1p5.setWas("27.00");
		priceReduction1p5.setPrice(price1p5);
	}
	
	@Test
	public void testDefaultColourSwatch() {
		final Product product = new Product();
		final JsonNode parsedProduct = mapper.valueToTree(product);
		
		assertFalse(parsedProduct.get("colorSwatches").isNull(),"colourSwatches default should not be null");
		assertTrue(parsedProduct.get("colorSwatches").isArray(),"colourSwatches default should be array");
	}
	
	@Test
	public void testSetColourSwatchToNull() {
		final Product product = new Product();
		
		product.setColorSwatches(null);
		
		final JsonNode parsedProduct = mapper.valueToTree(product);
		
		assertFalse(parsedProduct.get("colorSwatches").isNull(),"colourSwatches should not be set to null");
		assertTrue(parsedProduct.get("colorSwatches").isArray(),"colourSwatches should be array");
	}
	
	@Test
	/**
	 * Test parsing product from static JSON file
	 * @throws Exception
	 */
	public void testParsingProductFromJson() throws Exception {
		final InputStream is = this.getClass().getClassLoader().getResourceAsStream("product.json");
		final Product product = mapper.readValue(is, Product.class);
				
		assertEquals("3525081", product.getProductId(), "Product ID");
		assertEquals("hush Marble Panel Maxi Dress, Multi", product.getTitle(), "Title");
		
		final Price price = product.getPrice();
				
		assertEquals("99.00", price.getWas(), "Price was");
		assertEquals("59.00", price.getNow(), "Price now");
		assertEquals("85.00", price.getThen1(), "Price then1");
		assertEquals("69.00", price.getThen2(), "Price then2");
		assertEquals("GBP", price.getCurrency(), "Price currency");
	}
	
	@Test
	public void testPriceReduction() {
		assertFalse(noPriceReduction1.hasPriceReduction(), "Has price reduction");
		assertFalse(noPriceReduction2.hasPriceReduction(), "Has price reduction");
		assertTrue(priceReduction10.hasPriceReduction(), "Has price reduction");
		assertTrue(priceReduction15.hasPriceReduction(), "Has price reduction");
		assertTrue(priceReduction1p5.hasPriceReduction(), "Has price reduction");
	}
	
	//Comparing price reductions
	
	@Test
	public void testSamePriceReductions() {
		assertEquals(0, noPriceReduction1.comparePriceReduction(noPriceReduction1));
		assertEquals(0, noPriceReduction1.comparePriceReduction(noPriceReduction2));
		assertEquals(0, noPriceReduction2.comparePriceReduction(noPriceReduction1));
		assertEquals(0, noPriceReduction2.comparePriceReduction(noPriceReduction2));
		
		assertEquals(0, priceReduction15.comparePriceReduction(priceReduction15again));
		assertEquals(0, priceReduction15again.comparePriceReduction(priceReduction15));
	}
	
	@Test
	public void testGreaterPriceReductions() {
		assertEquals(1, noPriceReduction1.comparePriceReduction(priceReduction10));
		assertEquals(1, noPriceReduction1.comparePriceReduction(priceReduction15));
		assertEquals(1, noPriceReduction1.comparePriceReduction(priceReduction1p5));
		
		assertEquals(1, priceReduction1p5.comparePriceReduction(priceReduction10));
		
		assertEquals(1, priceReduction1p5.comparePriceReduction(priceReduction15));
		assertEquals(1, priceReduction10.comparePriceReduction(priceReduction15));
	}
	
	@Test
	public void testLesserPriceReductions() {
		assertEquals(-1, priceReduction10.comparePriceReduction(noPriceReduction1));
		assertEquals(-1, priceReduction15.comparePriceReduction(noPriceReduction1));
		assertEquals(-1, priceReduction1p5.comparePriceReduction(noPriceReduction1));
		
		assertEquals(-1, priceReduction10.comparePriceReduction(priceReduction1p5));
		
		assertEquals(-1, priceReduction15.comparePriceReduction(priceReduction1p5));
		assertEquals(-1, priceReduction15.comparePriceReduction(priceReduction10));
	}
}
