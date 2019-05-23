package jltechtest.data.parsing;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 *  Custom Jackson deserialiser that ignore errors and returns null allowing
 *  parsing of collections of objects with individual errors without the entire 
 *  parse process failing
 * @author jason
 *
 * @param <T>
 */
public class NonBlockingDeserializer<T> extends JsonDeserializer<T> {
    private static final Logger LOG = LogManager.getLogger();

    private StdDeserializer<T> delegate;

    public NonBlockingDeserializer(final StdDeserializer<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T deserialize(final JsonParser parser, final DeserializationContext context)
    		throws IOException, JsonProcessingException {
        try {
            return delegate.deserialize(parser, context);

        } catch (JsonProcessingException e) {
        	// According to spec - ignore invalid values
        	LOG.warn("Parsing error: {}",e.getMessage());
            return delegate.getNullValue(context);
        }
    }
}
