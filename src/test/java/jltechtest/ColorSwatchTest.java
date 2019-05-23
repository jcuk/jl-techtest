package jltechtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jltechtest.data.ColorSwatch;

public class ColorSwatchTest {
	private static final ObjectMapper mapper = new ObjectMapper();

//	/**
//	 * Spot check RGB mappings are not case sensitive
//	 */
//	@Test
//	public void testValidColourToRGBMappings() {
//		this.testRGBColour("Chocolate", "D2691E");
//		this.testRGBColour("chocolate", "D2691E");
//		
//		this.testRGBColour("deepskyblue", "00BFFF");
//		this.testRGBColour("DeepSkyBlue", "00BFFF");
//		
//		this.testRGBColour("KHAKI", "F0E68C");
//		this.testRGBColour("khaki", "F0E68C");
//		
//		this.testRGBColour("lime", "00FF00");
//		this.testRGBColour("lIMe", "00FF00");
//	}
//	
//	/**
//	 * Check colours are allowed trailing and leading spaces
//	 */
//	@Test
//	public void testValidColourToRGBMappingsWithSpaces() {
//		this.testRGBColour(" mintcream", "F5FFFA");
//		this.testRGBColour("mintcream ", "F5FFFA");
//		this.testRGBColour(" mintcream ", "F5FFFA");
//		this.testRGBColour("mintcream", "F5FFFA");
//	}
//	
//	/**
//	 * Check invalid colours return space (as per specification) and do not throw exceptions
//	 */
//	@Test
//	public void testInvalidValidColourToRGBMappings() {
//		this.testRGBColour("mintycream", "");
//		this.testRGBColour("depblue ", "");
//		this.testRGBColour(" ", "");
//		this.testRGBColour("", "");
//		this.testRGBColour(null, "");
//	}
//
//	/** Helper method to test if when a swatch is set to a given colour, the rgb colour code
//	 * is computed as expected.
//	 * @param colour
//	 * @param expectedRGBCode
//	 */
//	private void testRGBColour(final String colour, final String expectedRGBCode) {
//		final ColorSwatch swatch = new ColorSwatch();
//		swatch.setColor(colour);
//		
//		final JsonNode parsedSwatch = mapper.valueToTree(swatch);
//
//		assertEquals(expectedRGBCode, parsedSwatch.get("rgbColor").asText(), "Colour value");
//	}
}
