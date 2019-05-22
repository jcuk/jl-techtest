package jltechtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jltechtest.data.Price;
import jltechtest.data.Product;

public class ProductTest {
	private static final ObjectMapper mapper = new ObjectMapper();
	
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
}
