package jltechtest.formatter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jltechtest.data.Price;

/**
 * Formatter for pricing discounts. Takes a {@link jltechtest.data.Price
 * Product} object and returns a price label for that object in the selected
 * format
 * 
 * @author jason
 *
 */
public class PriceLabelFormatter {
	private static final Logger LOG = LogManager.getLogger();

	/**
	 * Threshold for showing currency as decimal rather than integer e.g. 9.99 v 10
	 */
	private static final BigDecimal decimalThreshold = new BigDecimal(10);

	public enum PriceFormat {
		WasNow((p) -> {
			return String.format("Was %s, now %s", PriceLabelFormatter.formatCurrency(p.getWas(), p.getCurrency()),
					PriceLabelFormatter.formatCurrency(p.getNow(), p.getCurrency()));
		}),

		WasThenNow((p) -> {
			String then = p.getThen2();
			if (then == null || then.isEmpty()) {
				then = p.getThen1();
				// No then1 or then 2, default to the WasNow format (omit the 'then..')
				if (then == null || then.isEmpty()) {
					return WasNow.getFormat(p);
				}
			}

			return String.format("Was %s, then %s, now %s",
					PriceLabelFormatter.formatCurrency(p.getWas(), p.getCurrency()),
					PriceLabelFormatter.formatCurrency(then, p.getCurrency()),
					PriceLabelFormatter.formatCurrency(p.getNow(), p.getCurrency()));

		}),

		PercDiscount((p) -> {
			//Use doubles to calculate percentages to avoid Non-terminating decimal expansion exception during
			// division. (Discount percentages are rounded down, so we are not concerned with BigDecimal
			// precision)
			final double was = Double.valueOf(p.getWas());
			final double now = Double.valueOf(p.getNow());
			
			final int discount = (int) (((was - now)/was) * 100);
			
			return String.format("%d%% off - now %s",
				discount,
				PriceLabelFormatter.formatCurrency(p.getNow(), p.getCurrency()));
		});

		final Function<Price, String> format;

		private PriceFormat(Function<Price, String> format) {
			this.format = format;
		}

		private String getFormat(final Price price) {
			return this.format.apply(price);
		}
	}

	private PriceLabelFormatter() {
	}

	/**
	 * Format a price to one of the predefined formats
	 * 
	 * @param price
	 * @param priceFormat
	 * @return
	 */
	public static String format(final Price price, final PriceFormat priceFormat) {
		try {
			return priceFormat.getFormat(price);
		} catch (Exception e) {
			LOG.error("Error formatting currency in price label", e);
		}
		return "";
	}

	/**
	 * Format a string based currency and value. Values >= 10 are returned as
	 * integers e.g. 10.99 would return £10. Values < 10 are returned as decimal to
	 * a fixed 2dp e.g. 4.5 would return £4.50
	 * 
	 * NB Unteseted other than EUR and GBP
	 * 
	 * @param value        String value of currency e.g. "1.20", "2", ".99", "200"
	 *                     etc.
	 * @param currencyCode ISO Currency code
	 * @return
	 */
	public static String formatCurrency(final String value, final String currencyCode) {
		final Currency currency = Currency.getInstance(currencyCode);
		final String symbol = currency.getSymbol();

		final BigDecimal amount = new BigDecimal(value);

		if (amount.compareTo(decimalThreshold) < 0 || !isIntegerValue(amount)) {
			// Format currency as decimal
			return String.format("%s%.2f", symbol, amount.doubleValue());
		} else {
			// Format currency as integer
			return String.format("%s%d", symbol, amount.intValue());
		}
	}

	/** Check if a given <code>BigDecimal</code> is an integer or not
	 * 
	 * @param decimal
	 * @return true iff the given value is an integer
	 */
	private static boolean isIntegerValue(final BigDecimal decimal) {
		return decimal.stripTrailingZeros().scale() <= 0;
	}

}
