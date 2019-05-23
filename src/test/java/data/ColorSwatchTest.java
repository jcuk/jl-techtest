package data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import jltechtest.data.ColorSwatch;

public class ColorSwatchTest {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void testParsingFromJSON() throws Exception {
		final InputStream is = this.getClass().getClassLoader().getResourceAsStream("colourswatch.json");
		final ColorSwatch swatch = mapper.readValue(is, ColorSwatch.class);
				
		assertEquals("Red", swatch.getBasicColor(), "Basic Colour");
		assertEquals("237494589", swatch.getSkuId(), "skuId");

	}

}
