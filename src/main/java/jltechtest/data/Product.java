package jltechtest.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Data placeholder for Product JSON object
 * @author jason
 *
 */
public class Product {
	private String productId = "";
	private String title = "";
	private ColorSwatch[] colorSwatches = new ColorSwatch[0];
	private Price price;
	
	public String getProductId() {
		return productId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public ColorSwatch[] getColorSwatches() {
		return colorSwatches;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setColorSwatches(ColorSwatch[] colorSwatches) {
		if (colorSwatches == null) {
			colorSwatches = new ColorSwatch[0];
		}
		this.colorSwatches = colorSwatches;
	}
	
	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}
	
}