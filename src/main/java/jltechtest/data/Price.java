package jltechtest.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Data placehoder object for Price JSON object
 * @author jason
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Price {
	private String was;
	private String then1;
	private String then2;
	private String now;
	private String currency;
	
	public String getWas() {
		return was;
	}
	
	public String getThen1() {
		return then1;
	}
	
	public String getThen2() {
		return then2;
	}
	
	public String getNow() {
		return now;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setWas(String was) {
		this.was = was;
	}
	
	public void setThen1(String then1) {
		this.then1 = then1;
	}
	
	public void setThen2(String then2) {
		this.then2 = then2;
	}
	
	public void setNow(String now) {
		this.now = now;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
