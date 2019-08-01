package nl.rabobank.statementprocessor.configuration;

import nl.rabobank.statementprocessor.parser.TransactionXmlParser;
import nl.rabobank.statementprocessor.properties.CustomProperties;
import nl.rabobank.statementprocessor.service.ParserService;
import nl.rabobank.statementprocessor.service.ValidationService;
import nl.rabobank.statementprocessor.parser.TransactionCsvParser;
import nl.rabobank.statementprocessor.validator.StatementValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
		"nl.rabobank.statementprocessor.factory",
		"nl.rabobank.statementprocessor.properties"
})
@EnableConfigurationProperties
public class TestConfiguration {
	@Bean(name = "testParserService")
	public ParserService parserService(final TransactionXmlParser transactionXmlParser, final TransactionCsvParser transactionCsvParser) {
		return new ParserService(transactionXmlParser, transactionCsvParser);
	}
	
	@Bean(name = "testValidationService")
	public ValidationService validationService(final StatementValidator validator, final CustomProperties customProperties) {
		return new ValidationService(validator, customProperties);
	}
}
