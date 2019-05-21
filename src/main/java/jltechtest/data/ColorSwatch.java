package jltechtest.data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value={ "rgbColor" }, allowGetters=true, ignoreUnknown=true)
/**
 * Data placeholder object for ColorSwatch JSON object
 * @author jason
 *
 */
public class ColorSwatch {
	private String color;
	private String skuid;
	
	public String getColor() {
		return color;
	}
	
	/** 
	 * @return <code>String</code> containing the <a href="https://www.w3.org/wiki/CSS/Properties/color/keywords">
	 * w3.org colour</a> computed from {@link #getColor() getColor()}
	 * 
	 */
	public String getRgbColor() {
		if (color == null || color.isEmpty()) {
			return "";
		}
		
		return RGBColourHelper.colourToRGB(color);
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
}