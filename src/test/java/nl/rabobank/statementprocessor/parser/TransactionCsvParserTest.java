package nl.rabobank.statementprocessor.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import nl.rabobank.statementprocessor.factory.TestObjectFactory;
import nl.rabobank.statementprocessor.entity.Statement;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TransactionCsvParserTest {
	private TransactionCsvParser parser;
	
	@Before
	public void setUp() {
		this.parser = new TransactionCsvParser();
	}
	
	@Test
	public void whenTransactionFileIsValidThenParserResultShouldHaveNoErrors() throws IOException {

		String fileContent = TestObjectFactory.getValidCsvFileText();
		ResultHolder<Statement> resultHolder = this.parser.parse(fileContent);
		
		this.assertValidParserResults(resultHolder);
		assertThat("Transaction list should not be empty.", resultHolder.getResult().getTransactions(), is(not(empty())));
	}
	
	@Test
	public void whenTransactionFileHasEmptyRecordsThenParserResultShouldHaveNoErrors() throws IOException {

		String fileContent = TestObjectFactory.getEmptyCsvFileText();
		
		ResultHolder<Statement> resultHolder = this.parser.parse(fileContent);
		
		this.assertValidParserResults(resultHolder);
		assertThat("Transaction list should be empty.", resultHolder.getResult().getTransactions(), is(empty()));
	}
	
	private <T> void assertValidParserResults(final ResultHolder<T> resultHolder) {
		assertNotNull("IParser result should not be null.", resultHolder);
		assertNotNull("Result object should not be null.", resultHolder.getResult());
		

	}
}