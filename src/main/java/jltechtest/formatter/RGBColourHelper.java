package jltechtest.formatter;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Helper class to map <a href="https://www.w3.org/wiki/CSS/Properties/color/keywords">w3.org colour names</a> to RGB values.
 * The data is read in pairs from local file in the format <colour>=<rr><gg><bb> e.g. aliceblue=f0f8ff
 * 
 * @author jason
 *
 */
@Component
@ConfigurationProperties("")
@EnableConfigurationProperties
@PropertySource("classpath:w3colours.properties")
public class RGBColourHelper {
	private static final Logger LOG = LogManager.getLogger();
		
	private Map<String, String> colours = new HashMap<>();
	
    public void setColours(Map<String, String> colours) {
		this.colours = colours;
	}

	public Map<String, String> getColours() {
        return colours;
    }
	
	@EventListener(ApplicationReadyEvent.class)
	public void initMessage() {
		LOG.info("Initalised RGB colour helper with {} colours",colours.size());
	}

	/**
	 * Given a w3c colour, return the RGB code for that colour if it exists
	 * @param w3cColour
	 * @return RGB code for that colour if it exists, otherwise empty string
	 */
	public String colourToRGB(String w3cColour) {
		if (w3cColour == null) {
			return "";
		}
		final String rgb = colours.get(w3cColour.trim().toLowerCase());
		return rgb==null?"":rgb.toUpperCase();
	}
}
