package jltechtest.formatter;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jltechtest.formatter.RGBColourHelper;

@SpringBootTest(classes= {RGBColourHelper.class})
public class RGBColourHelperTest {
	@Autowired
	private RGBColourHelper colourHelper;
		
	/**
	 * Spot check RGB mappings are not case sensitive
	 */
	@Test
	public void testValidColourToRGBMappings() {
		assertEquals("Chocolate", "D2691E", colourHelper.colourToRGB("Chocolate"));
		assertEquals("chocolate", "D2691E", colourHelper.colourToRGB("chocolate"));
		
		assertEquals("deepskyblue", "00BFFF", colourHelper.colourToRGB("deepskyblue"));
		assertEquals("DeepSkyBlue", "00BFFF", colourHelper.colourToRGB("DeepSkyBlue"));
		
		assertEquals("KHAKI", "F0E68C", colourHelper.colourToRGB("KHAKI"));
		assertEquals("khaki", "F0E68C", colourHelper.colourToRGB("khaki"));
		
		assertEquals("lime", "00FF00", colourHelper.colourToRGB("lime"));
		assertEquals("lIMe", "00FF00", colourHelper.colourToRGB("lIMe"));
	}
	
	/**
	 * Check colours are allowed trailing and leading spaces
	 */
	@Test
	public void testValidColourToRGBMappingsWithSpaces() {
		assertEquals("colour with spaces", "F5FFFA", colourHelper.colourToRGB(" mintcream"));
		assertEquals("colour with spaces", "F5FFFA", colourHelper.colourToRGB( "mintcream "));
		assertEquals("colour with spaces", "F5FFFA", colourHelper.colourToRGB(" mintcream "));
		assertEquals("colour with spaces", "F5FFFA", colourHelper.colourToRGB("mintcream"));
	}
	
	/**
	 * Check invalid colours return space (as per specification) and do not throw exceptions
	 */
	@Test
	public void testInvalidValidColourToRGBMappings() {
		assertEquals("invalid colour", "", colourHelper.colourToRGB("mintycream"));
		assertEquals("invalid colour", "", colourHelper.colourToRGB("depblue "));
		assertEquals("invalid colour", "", colourHelper.colourToRGB(" "));
		assertEquals("invalid colour", "", colourHelper.colourToRGB(""));
		assertEquals("invalid colour", "", colourHelper.colourToRGB(null));
	}
}
