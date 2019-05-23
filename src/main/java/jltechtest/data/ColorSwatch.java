package jltechtest.data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * Data placeholder object for ColorSwatch JSON object
 * @author jason
 *
 */
public class ColorSwatch {
	private String color;
	private String skuid;
	private String rgbColour;
	
	public String getColor() {
		return color;
	}
	
	public String getRgbColor() {
		return rgbColour;
	}
	
	public String getSkuid() {
		return skuid;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public void setSkuid(String skuid) {
		this.skuid = skuid;
	}
	
	public void setRgbColor(String rgbColour) {
		this.rgbColour = rgbColour;
	}
}