package nl.rabobank.statementprocessor.parser;

import nl.rabobank.statementprocessor.entity.Statement;
import nl.rabobank.statementprocessor.entity.Transaction;
import nl.rabobank.statementprocessor.properties.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TransactionCsvParser resonsible for parsing
 */
public class TransactionCsvParser implements IParser<String, Statement> {
	private static final Logger logger = LoggerFactory.getLogger(TransactionCsvParser.class);
	
	/**
	 * Parses the CSV content into transaction bucket.
	 * @param content content to be parsed.
	 * @return parser result.
	 */
	@Override
	public ResultHolder<Statement> parse(final String content) {
		// Initializes error list.
		List<String> parserErrorList = new ArrayList<>();
		
		// Splits the content string into lines.
		String[] records = content.split("\\r?\\n");

		List<Transaction> transactionList = Arrays.stream(records)
											      .skip(1)
											      .map(line -> this.populateRow(line, parserErrorList))
		                                          .filter(Objects::nonNull)
											      .collect(Collectors.toList());

		Statement statement = new Statement();
		statement.setTransactions(transactionList);
		return new ResultHolder<>(statement, parserErrorList);
	}
	
	private Transaction parseRow(final String[] dataTokens) {

		Transaction transaction = new Transaction();

		transaction.setTransactionReference(Long.parseLong(dataTokens[0]));
		transaction.setAccountNumber(dataTokens[1]);
		transaction.setDescription(dataTokens[2]);
		transaction.setStartBalance(new BigDecimal(dataTokens[3]));
		transaction.setMutation(new BigDecimal(dataTokens[4]));
		transaction.setEndBalance(new BigDecimal(dataTokens[5]));
		
		return transaction;
	}
	
	private Transaction populateRow(final String dataLine, final List<String> parserErrorList) {

		String[] tokens = dataLine.split(Constants.CSV_DELIMITER);
		
		try {
			return this.parseRow(tokens);
		} catch (Exception e) {
			logger.error("Unable to parse record: " + dataLine, e);
			parserErrorList.add("Unable to parse record: " + dataLine);
			return null;
		}
	}
}
