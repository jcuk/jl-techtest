package jltechtest.data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * Data placeholder object for ColorSwatch JSON object
 * @author jason
 *
 */
public class ColorSwatch {
	private String basicColor;
	private String skuId;
	private String rgbColour;
	
	public String getBasicColor() {
		return basicColor;
	}
	
	public String getRgbColor() {
		return rgbColour;
	}
	
	public String getSkuId() {
		return skuId;
	}
	
	public void setBasicColor(String basicColor) {
		this.basicColor = basicColor;
	}
	
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	
	public void setRgbColor(String rgbColour) {
		this.rgbColour = rgbColour;
	}
}