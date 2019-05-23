package jltechtest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jltechtest.data.Product;
import jltechtest.data.ProductsPage;

/** Class for fetching products from an paginated endpoint.
 * @author jason
 *
 */
@Service
public class ProductFetcher {
	private static final Logger LOG = LogManager.getLogger();
	
	@Autowired
	private ProductPageFetcher productPageFetcher;
	
	/** Get all products from the endpoint. The data is paginated, so download the first page, extract the 
	 * number of pages and then download each page
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<Product> getProducts() {
		final List<Product> products = new ArrayList<>();
		
		//Download the first page to grab the first lot of data and to see the page count of how
		// many pages we need to download
		final CompletableFuture<ProductsPage> firstProductsPage = productPageFetcher.getProductPage(null);
		
		int pages;
		try {
			LOG.info("Downloaded first page with {} products", firstProductsPage.get().getProducts().size());
			products.addAll(firstProductsPage.get().getProducts());
			
			pages = firstProductsPage.get().getPagesAvailable();
			LOG.info("Downloading {} pages of products",pages);
		} catch (InterruptedException | ExecutionException e) {
			LOG.error("Error downloading first page of products",e);
			return null;
		}

		
		//Set up all the parallel download jobs
		final List<CompletableFuture<ProductsPage>>productPages = new ArrayList<>();
		for (int page=1;page<pages;page++) {
			LOG.info("Downloading page {} of {} of products",page, pages);
			productPages.add(productPageFetcher.getProductPage(page));
		}
		
		//Kick off all the downloads. These will execute in parallel using the 
		// ThreadPoolTaskExecutor we configured in AppConfig
		LOG.info("Starting downloads of products",pages);
		productPages.stream()
			.forEach(p->{
				try {
					LOG.info("Downloaded page {} of {} with {} products",
							p.get().getPageId(),
							pages,
							p.get().getProducts().size());
					
					products.addAll(p.get().getProducts());
				} catch (InterruptedException | ExecutionException e) {
					//Individual page errors will be ignored
					LOG.error("Error retrieving page ",e);
				}
			});
		
		return products;
	}
}
