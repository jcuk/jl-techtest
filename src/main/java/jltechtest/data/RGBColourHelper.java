package jltechtest.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Helper class to map <a href="https://www.w3.org/wiki/CSS/Properties/color/keywords">w3.org colour names</a> to RGB values.
 * The data is read in pairs from local file in the format <colour>=<rr><gg><bb> e.g. aliceblue=f0f8ff
 * 
 * @author jason
 *
 */
public class RGBColourHelper {
	private static final Logger LOG = LogManager.getLogger();
	
	private static final String COLOUR_PROPERTY_FILE = "w3colours.properties";
	private static Map<String, String> colours = new HashMap<>();

	static {
		final Properties properties = new Properties();

		try {
			properties.load(ClassLoader.getSystemClassLoader().getResourceAsStream(COLOUR_PROPERTY_FILE));

			colours.putAll(properties.entrySet().stream()
					.collect(Collectors.toMap(
							entry -> entry.getKey().toString().toLowerCase(),
							entry -> entry.getValue().toString().toUpperCase())));
			
			LOG.info("Initalised RGB lookup with {} colours",colours.size());
		} catch (Exception e) {
			LOG.fatal("Failed to initalise RGB Helper",e);
			throw new RuntimeException("Error initalising RGBColourHelper",e);
		}
	}
	
	private RGBColourHelper() {
	}

	/**
	 * Given a w3c colour, return the RGB code for that colour if it exists
	 * @param colour
	 * @return RGB code for that colour if it exists, otherwise empty string
	 */
	public static String colourToRGB(String colour) {
		if (colour == null) {
			return "";
		}
		final String rgb = colours.get(colour.trim().toLowerCase());
		return rgb==null?"":rgb;
	}
}
