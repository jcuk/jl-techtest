package data;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
