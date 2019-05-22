package jltechtest;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jltechtest.data.Price;

/**
 * Helper class for currency label formatting. Takes a {@link jltechtest.data.Price
 * Product} object and returns a price label for that object in the selected format
 * 
 * @author jason
 *
 */
public class PriceLabelFormatter {
	private static final Logger LOG = LogManager.getLogger();

	/** Threshold for showing currency as decimal rather than integer e.g. 9.99 v 10 */
	private static final BigDecimal decimalThreshold = new BigDecimal(10);
	
	public enum PriceFormat {
		WasNow(
			(p)-> {
				//
				return String.format("Was %s, now %s",
					PriceLabelFormatter.formatCurrency(p.getWas(), p.getCurrency()),
					PriceLabelFormatter.formatCurrency(p.getNow(), p.getCurrency()));
			}
		),
		
		WasThenNow(
			(p)-> {
				//"Was %s, then %s, now %s",
				return "";
			}
		),
		
		PercDiscount(
			(p)-> {
//				"%d%%off - now %s",
				return "";
			}
		);
				
		final Function<Price, String>format;
		
		private PriceFormat(Function<Price, String> format) {
			this.format = format;
		}
		
		private String getFormat(final Price price) {
			return this.format.apply(price);
		}
	}
	
	private PriceLabelFormatter() {
	}

	public static String format(final Price price, final PriceFormat priceFormat) {
		try {
			return priceFormat.getFormat(price);
		} catch (Exception e) {
			LOG.error("Error formatting currency in price label",e);
		}
		return "";
	}
	
	public static String formatCurrency(final String value, final String currencyCode) {
		final Currency currency = Currency.getInstance(currencyCode);
		final String symbol = currency.getSymbol();
		
		final BigDecimal amount = new BigDecimal(value);
		
		if (amount.compareTo(decimalThreshold) < 0) {
			//Format currency as decimal
			return String.format("%s%.2f", symbol, amount.doubleValue());
		} else {
			//Format currency as integer
			return String.format("%s%d", symbol, amount.intValue());
		}
	}

}
