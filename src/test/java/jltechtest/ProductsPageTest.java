package jltechtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import jltechtest.data.ProductsPage;

public class ProductsPageTest {
	private static final ObjectMapper mapper = new TollerantObjectMapper().getMapper();
	
	@Test
	/**
	 * Test parsing paginated products page from static JSON file pulled from test endpoint
	 * @throws Exception
	 */
	public void testParsingProductsFromJson() throws Exception {
		
		final InputStream is = this.getClass().getClassLoader().getResourceAsStream("products.json");
		final ProductsPage products = mapper.readValue(is, ProductsPage.class);
				
		assertEquals(5648, products.getResults(), "Number of results");
		assertEquals(113, products.getPagesAvailable(), "Pages avaialable");
		
		assertEquals(52, products.getProducts().size(), "Number of products");				
	}

}
