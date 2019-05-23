package jltechtest.data;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsPage {
	private ArrayList<Product> products;
	private int results;
	private int pagesAvailable;
	
	public ArrayList<Product> getProducts() {
		return products;
	}
	
	public int getResults() {
		return results;
	}
	
	public int getPagesAvailable() {
		return pagesAvailable;
	}
	
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	
	public void setResults(int results) {
		this.results = results;
	}
	
	public void setPagesAvailable(int pagesAvailable) {
		this.pagesAvailable = pagesAvailable;
	}

}
