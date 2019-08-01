package nl.rabobank.statementprocessor.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;

/**
 * Base XML IParser to parse strings into the generic object type.
 *
 * @param <T> target object type.
 */
public class BaseXmlParser<T> implements IParser<String, T> {
	private static final Logger logger = LoggerFactory.getLogger(BaseXmlParser.class);
	
	private final Class<T> clazz;
	private final XmlMapper mapper;
	
	/**
	 * Constructor to define the class type of the generic variable.
	 *
	 * @param clazz class of the generic type.
	 */
	public BaseXmlParser(final Class<T> clazz) {
		super();
		
		this.clazz = clazz;
		this.mapper = new XmlMapper();
	}
	
	/**
	 * Parser implementation, parses string content into result object.
	 *
	 * @param content text content to be parsed.
	 * @return parser result.
	 */
	@Override
	public ResultHolder<T> parse(final String content) {
		try {
			return new ResultHolder<>(mapper.readValue(content, clazz));
		} catch (IOException e) {
			String errorMessage = "Failed to parse : " + clazz.getSimpleName();
			logger.error(errorMessage, e);
			return new ResultHolder<>(null, Collections.singletonList(errorMessage));
		}
	}
}
