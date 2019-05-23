package jltechtest.data;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data placeholder for Product JSON object
 * @author jason
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
	private String productId = "";
	private String title = "";
	private ColorSwatch[] colorSwatches = new ColorSwatch[0];
	private Price price;
	private String priceLabel;
	private String nowPrice;
	
	public String getProductId() {
		return productId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public ColorSwatch[] getColorSwatches() {
		return colorSwatches;
	}
	
	public String getPriceLabel() {
		return priceLabel;
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
	
	@JsonIgnore
	public Price getPrice() {
		return price;
	}

	@JsonProperty
	public void setPrice(Price price) {
		this.price = price;
	}

	public void setPriceLabel(String priceLabel) {
		this.priceLabel = priceLabel;
	}

	public String getNowPrice() {
		return nowPrice;
	}

	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}
	
	/** Return true if the product has a price reduction i.e. if both the was and the
	 * now field are present on the price
	 * @param product
	 * @return
	 */
	public boolean hasPriceReduction() {
		return 	this.getPrice() != null &&
				this.getPrice().getWas() != null &&
				this.getPrice().getNow() != null &&
				!this.getPrice().getNow().isEmpty() &&
				!this.getPrice().getWas().isEmpty();
	}
	
	/** Compare the price reduction of two products. If this product has a greater
	 * price reduction, return 1. If the two products have equal reductions, return 0
	 * else return -1;
	 * 
	 * @param product2
	 * @return
	 */
	public int comparePriceReduction(final Product product2) {
		final BigDecimal reduction = this.getPriceReduction();
		final BigDecimal p2reduction = product2.getPriceReduction();
		
		if (reduction == null && p2reduction==null) {
			return 0;
		}
		
		if (reduction == null) {
			return 1;
		}
		
		if (p2reduction == null) {
			return -1;
		}
		
		return p2reduction.compareTo(reduction);
		
	}
	
	/** Gets the price reduction. If price now or next are not parseable, returns null
	 * 
	 * @return
	 */
	@JsonIgnore
	public BigDecimal getPriceReduction() {
		try {
			final BigDecimal now = new BigDecimal(this.price.getNow());
			final BigDecimal was = new BigDecimal(this.price.getWas());
			return was.subtract(now);
		} catch (Exception e) {
			//Ignore Parsing andØ NPE
			return null;
		}
	}
	
}