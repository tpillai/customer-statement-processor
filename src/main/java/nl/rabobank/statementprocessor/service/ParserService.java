package nl.rabobank.statementprocessor.service;

import nl.rabobank.statementprocessor.entity.Statement;
import nl.rabobank.statementprocessor.parser.IParser;
import nl.rabobank.statementprocessor.parser.ResultHolder;
import nl.rabobank.statementprocessor.parser.TransactionCsvParser;
import nl.rabobank.statementprocessor.parser.TransactionXmlParser;
import nl.rabobank.statementprocessor.properties.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 *  ParserService responsible for identifying right parser and parsing file
 */
@Service
public class ParserService {
	private static final Logger logger = LoggerFactory.getLogger(ParserService.class);
	
	private final TransactionXmlParser transactionXmlParser;
	private final TransactionCsvParser transactionCsvParser;
	
	@Autowired
	public ParserService(final TransactionXmlParser transactionXmlParser, final TransactionCsvParser transactionCsvParser) {
		this.transactionXmlParser = transactionXmlParser;
		this.transactionCsvParser = transactionCsvParser;
	}
	
	/**
	 * Returns the associated parser for the specified file.
	 *
	 * @param contentType content type.
	 *
	 * @return associated parser for the specified file.
	 */
	public Optional<IParser<String, Statement>> getValidParser(final String contentType) {
		// If the type is not XML or CSV, returns null.
		switch (contentType) {
			case Constants.APPLICATION_XML:
			case Constants.TEXT_XML:
				return Optional.of(transactionXmlParser);
			case Constants.TEXT_CSV:
				return Optional.of(transactionCsvParser);
			default:
				return Optional.empty();
		}
	}
	
	/**
	 * Returns if the file is parsable or not.
	 *
	 * @param contentType content type.
	 *
	 * @return TRUE if the file is parsable; FALSE otherwise.
	 */
	public boolean isParsable(final String contentType) {
		return this.getValidParser(contentType).isPresent();
	}
	
	/**
	 * Returns the parsing result for the specified file.
	 *
	 * @param contentType content type.
	 * @param content content.
	 *
	 * @return parser result for the file content.
	 */
	public ResultHolder<Statement> parseFile(final String contentType, final String content) {
		ResultHolder<Statement> resultHolder;
		
		// Get the associated parser for the specified file.
		Optional<IParser<String, Statement>> parserOpt = this.getValidParser(contentType);
		
		try {
			if (parserOpt.isPresent()) {
				// parses the content if parser is present
				resultHolder = parserOpt.get().parse(content);
			} else {
				// Adds error message
				resultHolder = new ResultHolder<>(null, Collections.singletonList(" Valid parser not found for the file."));
			}
		} catch (Exception e) {

			String errorMessage = "Unable to parse the uploaded file content.";
			logger.error(errorMessage, e);
			resultHolder = new ResultHolder<>(null, Collections.singletonList(errorMessage));
		}
		
		// Returns the parser result.
		return resultHolder;
	}
}
