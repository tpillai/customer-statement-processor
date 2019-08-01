package nl.rabobank.statementprocessor.factory;

import nl.rabobank.statementprocessor.parser.TransactionCsvParser;
import nl.rabobank.statementprocessor.parser.TransactionXmlParser;
import nl.rabobank.statementprocessor.properties.CustomProperties;
import nl.rabobank.statementprocessor.validator.StatementValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Bean Factory to expose bean definitions.
 */
@Component
public class BeanFactory {

	@Bean
	public TransactionXmlParser xmlParser() {
		return new TransactionXmlParser();
	}
	
	/**
	 * TransactionCsvParser
	 *
	 * @return CSV IParser.
	 */
	@Bean
	public TransactionCsvParser csvParser() {
		return new TransactionCsvParser();
	}
	
	/**
	 * StatementValidator
	 *
	 * @return Validator.
	 */
	@Bean
	public StatementValidator validator() {
		return new StatementValidator();
	}

	//@Bean
//	public CustomProperties customProperties() {return new CustomProperties();}

}
