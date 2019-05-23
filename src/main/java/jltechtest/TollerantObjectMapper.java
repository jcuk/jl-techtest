package jltechtest;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import jltechtest.data.NonBlockingDeserializer;

/**
 *  Custom Jackson ObjectMapper that ignores string parsing errors e.g. when values that should be Strings
 *  are presented as JSON Nodes. NB This will still (correctly) fail to parse invalid JSON, this only
 *  copes with syntactically correct but semantically invalid JSON.
 * @author jason
 *
 */
@Component
public class TollerantObjectMapper {
	private final ObjectMapper mapper;
	
	public TollerantObjectMapper() {
		final SimpleModule module = new SimpleModule("nonBlockingParser", Version.unknownVersion());

		//Only copes with Strings. Other types could be added Inger.TYPE, Double.TYPE etc. if needed
		module.addDeserializer(String.class, new NonBlockingDeserializer<String>(
		                                     new StringDeserializer()));
		
		mapper = new ObjectMapper();
		mapper.registerModule(module);
		
	}
	
	public ObjectMapper getMapper() {
		return mapper;
	}

}
