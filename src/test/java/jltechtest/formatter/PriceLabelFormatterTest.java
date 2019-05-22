package jltechtest.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jltechtest.PriceLabelFormatter;
import jltechtest.data.Price;
import jltechtest.PriceLabelFormatter.PriceFormat;

public class PriceLabelFormatterTest {	
	private Price price;
	
	@BeforeEach
	public void setUp() {
		price = new Price();
	}
	
	@Test
	public void testFormatCurrencyInteger() {		
		assertEquals("£10", PriceLabelFormatter.formatCurrency("10.00", "GBP"));
		assertEquals("£10", PriceLabelFormatter.formatCurrency("10.01", "GBP"));
		assertEquals("£10", PriceLabelFormatter.formatCurrency("10.10", "GBP"));
		assertEquals("£10", PriceLabelFormatter.formatCurrency("10.99", "GBP"));
		assertEquals("£11", PriceLabelFormatter.formatCurrency("11.00", "GBP"));
		
		assertEquals("£22", PriceLabelFormatter.formatCurrency("22.00", "GBP"));
		assertEquals("£22", PriceLabelFormatter.formatCurrency("22.01", "GBP"));
		assertEquals("£22", PriceLabelFormatter.formatCurrency("22.50", "GBP"));
		assertEquals("£22", PriceLabelFormatter.formatCurrency("22.99", "GBP"));
		assertEquals("£22", PriceLabelFormatter.formatCurrency("22.09", "GBP"));
	}
	
	@Test
	public void testFormatCurrencyRounding() {
		assertEquals("£0.00", PriceLabelFormatter.formatCurrency("0", "GBP"));
		assertEquals("£0.01", PriceLabelFormatter.formatCurrency(".01", "GBP"));
		assertEquals("£1.00", PriceLabelFormatter.formatCurrency("1", "GBP"));
		assertEquals("£1.00", PriceLabelFormatter.formatCurrency("1.00", "GBP"));
	}
	
	@Test
	public void testFormatCurrencyEUR() {
		assertEquals("€0.00", PriceLabelFormatter.formatCurrency("0", "EUR"));
		assertEquals("€0.01", PriceLabelFormatter.formatCurrency(".01", "EUR"));
		assertEquals("€1.00", PriceLabelFormatter.formatCurrency("1", "EUR"));
		assertEquals("€1.00", PriceLabelFormatter.formatCurrency("1.00", "EUR"));
		
		assertEquals("€10", PriceLabelFormatter.formatCurrency("10.00", "EUR"));
		assertEquals("€10", PriceLabelFormatter.formatCurrency("10.01", "EUR"));
		assertEquals("€10", PriceLabelFormatter.formatCurrency("10.10", "EUR"));
		assertEquals("€10", PriceLabelFormatter.formatCurrency("10.99", "EUR"));
		assertEquals("€11", PriceLabelFormatter.formatCurrency("11.00", "EUR"));
	}
	
	@Test
	public void testFormatCurrencyInvalid() {
		assertThrows(NumberFormatException.class, () -> {
			PriceLabelFormatter.formatCurrency("", "GBP");
		});
		
		assertThrows(NullPointerException.class, () -> {
			PriceLabelFormatter.formatCurrency(null, "GBP");
		});
		
		assertThrows(NumberFormatException.class, () -> {
			PriceLabelFormatter.formatCurrency("1.1.1", "GBP");
		});
		
		assertThrows(NumberFormatException.class, () -> {
			PriceLabelFormatter.formatCurrency("one", "GBP");
		});
		
		assertThrows(NumberFormatException.class, () -> {
			PriceLabelFormatter.formatCurrency("1/2", "GBP");
		});
	}
	
	@Test
	public void testWasNowInteger() {
		price.setCurrency("GBP");
		price.setNow("85.00");
		price.setWas("110.00");
		price.setThen1("95.00");
		price.setThen2("90.00");
		
		final String label = PriceLabelFormatter.format(price, PriceFormat.WasNow);
		
		assertEquals("Was £110, now £85", label, "Was now");
	}
	
	@Test
	public void testWasNowIntegerWithRounding() {
		price.setCurrency("GBP");
		price.setNow("85.49");
		price.setWas("110.50");
		price.setThen1("95.00");
		price.setThen2("90.00");
		
		final String label = PriceLabelFormatter.format(price, PriceFormat.WasNow);
		
		assertEquals("Was £110, now £85", label, "Was now");
	}
	
	@Test
	public void testWasNowIntegerWithRounding2() {
		price.setCurrency("GBP");
		price.setNow("10.01");
		price.setWas("11.99");
		price.setThen1("11.50");
		price.setThen2("10.50");
		
		final String label = PriceLabelFormatter.format(price, PriceFormat.WasNow);
		
		assertEquals("Was £11, now £10", label, "Was now");
	}
	
	@Test
	public void testWasNowDecimal() {
		price.setCurrency("GBP");
		price.setNow("6.50");
		price.setWas("9.99");
		price.setThen1("8.50");
		price.setThen2("7.50");
		
		final String label = PriceLabelFormatter.format(price, PriceFormat.WasNow);
		
		assertEquals("Was £9.99, now £6.50", label, "Was now");
	}
	
	@Test
	public void testWasNowDecimal2() {
		price.setCurrency("GBP");
		price.setNow("1");
		price.setWas("9");
		price.setThen1("8");
		price.setThen2("7");
		
		final String label = PriceLabelFormatter.format(price, PriceFormat.WasNow);
		
		assertEquals("Was £9.00, now £1.00", label, "Was now");
	}
	
	/**
	 * Test formatting when prices drop below £1 (not mentioned in spec - assumed to be £0.xx)
	 */
	@Test
	public void testWasNowDecimal3() {
		price.setCurrency("GBP");
		price.setNow("0.40");
		price.setWas("0.99");
		price.setThen1("8");
		price.setThen2("7");
		
		final String label = PriceLabelFormatter.format(price, PriceFormat.WasNow);
		
		assertEquals("Was £0.99, now £0.40", label, "Was now");
	}
	
	@Test
	public void testWasNowInvalid() {
		price.setCurrency("GBP");
		price.setNow("0x40");
		price.setWas("0.99");
		price.setThen1("8");
		price.setThen2("7");
		
		String label = PriceLabelFormatter.format(price, PriceFormat.WasNow);
		
		assertEquals("", label, "Was now");
		
		price.setCurrency("GBP");
		price.setNow("0.40");
		price.setWas("");
		price.setThen1("8");
		price.setThen2("7");
		
		label = PriceLabelFormatter.format(price, PriceFormat.WasNow);
		
		assertEquals("", label, "Was now");
		
		price.setCurrency("GBP");
		price.setNow("0.40");
		price.setWas(null);
		price.setThen1("8");
		price.setThen2("7");
		
		label = PriceLabelFormatter.format(price, PriceFormat.WasNow);
		
		assertEquals("", label, "Was now");
	}
}
