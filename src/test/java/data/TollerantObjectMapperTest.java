package data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import jltechtest.data.Price;
import jltechtest.data.parsing.TollerantObjectMapper;

public class TollerantObjectMapperTest {
	private ObjectMapper mapper = new TollerantObjectMapper().getMapper();
	
	@Test
	public void testFaultyJSON() throws Exception {
		
		final String json = "{\n" + 
				"			\"was\": \"42.00\",\n" + 
				"			\"then1\": \"\",\n" + 
				"			\"then2\": \"\",\n" + 
				"			\"now\": {\n" + 				//Should be String
				"				\"from\": \"55.00\",\n" + 
				"				\"to\": \"100.00\"\n" + 
				"			},\n" + 
				"			\"uom\": \"\",\n" + 
				"			\"currency\": \"GBP\"\n" + 
				"		}";
		
		final Price price = mapper.readValue(json, Price.class);
		
		assertEquals("42.00", price.getWas(),"Was");
		assertEquals(null, price.getNow(),"Now");
	}
	
	@Test
	public void testValidJSON() throws Exception {
		
		final String json = "{\n" + 
				"			\"was\": \"42.00\",\n" + 
				"			\"then1\": \"\",\n" + 
				"			\"then2\": \"\",\n" + 
				"			\"now\": 13.00,\n" + 
				"			\"uom\": \"\",\n" + 
				"			\"currency\": \"GBP\"\n" + 
				"		}";
		
		final Price price = mapper.readValue(json, Price.class);
		
		assertEquals("42.00", price.getWas(),"Was");
		assertEquals("13.00", price.getNow(),"Now");
	}

}
