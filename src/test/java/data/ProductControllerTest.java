package data;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import jltechtest.ProductController;
import jltechtest.ProductFetchController;
import jltechtest.formatter.PriceLabelFormatter.PriceFormat;

@SpringBootTest(classes= {ProductController.class})
public class ProductControllerTest {
	
	@Autowired
	private ProductController productController;
	
	@MockBean
	private ProductFetchController fetchController;
	
	@Test
	public void testRequestParameterShowWasNow() throws Exception {
		productController.getDiscountedProducts("ShowWasNow");
		
		Mockito.verify(fetchController).getDiscountedProducts(PriceFormat.ShowWasNow);
	}
	
	@Test
	public void testRequestParameterShowWasThenNow() throws Exception {
		productController.getDiscountedProducts("ShowWasThenNow");
		
		Mockito.verify(fetchController).getDiscountedProducts(PriceFormat.ShowWasThenNow);
	}
	
	@Test
	public void testRequestParameterShowPercDiscount() throws Exception {
		//Spelling of discount is like this in specification
		productController.getDiscountedProducts("ShowPercDscount");
		
		Mockito.verify(fetchController).getDiscountedProducts(PriceFormat.ShowPercDscount);
	}
	
	@Test
	public void testRequestParameterInvalidValue() throws Exception {
		productController.getDiscountedProducts("invalidValue");
		
		Mockito.verify(fetchController).getDiscountedProducts(PriceFormat.ShowWasNow);
	}
	
	@Test
	public void testRequestParameterMissingValue() throws Exception {
		productController.getDiscountedProducts(null);
		
		Mockito.verify(fetchController).getDiscountedProducts(PriceFormat.ShowWasNow);
	}

}
