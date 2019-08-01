package nl.rabobank.statementprocessor.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

import nl.rabobank.statementprocessor.factory.TestObjectFactory;
import nl.rabobank.statementprocessor.entity.Statement;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TransactionXmlParserTest {
	private TransactionXmlParser parser;
	
	@Before
	public void setUp() {
		this.parser = new TransactionXmlParser();
	}
	
	@Test
	public void whenTransactionFileIsValidThenParserResultShouldHaveNoErrors() throws IOException {
		// Given all fields are valid
		String fileContent = TestObjectFactory.getValidXmlFileText();
		
		ResultHolder<Statement> resultHolder = this.parser.parse(fileContent);
		
		this.assertValidParserResults(resultHolder);
	}
	
	@Test
	public void whenTransactionFileHasEmptyRecordsThenParserResultShouldHaveNoErrors() throws IOException {
		// Given all fields are valid
		String fileContent = TestObjectFactory.getEmptyXmlFileText();
		
		ResultHolder<Statement> resultHolder = this.parser.parse(fileContent);
		
		this.assertValidParserResults(resultHolder);
	}
	
	@Test
	public void whenTransactionFileIsInvalidThenParserResultShouldHaveNoErrors() throws IOException {
		// Given all fields are valid
		String fileContent = TestObjectFactory.getUnparsableXmlFileText();
		
		ResultHolder<Statement> resultHolder = this.parser.parse(fileContent);
		
		this.assertInvalidParserResults(resultHolder, 1);
	}
	
	private <T> void assertValidParserResults(final ResultHolder<T> resultHolder) {
		assertNotNull("IParser result should not be null.", resultHolder);
		assertThat("IParser result errors should be empty.", resultHolder.getErrors(), is(empty()));
	}
	
	private <T> void assertInvalidParserResults(final ResultHolder<T> resultHolder, final int errorSize) {
		assertNotNull("IParser result should not be null.", resultHolder);
		assertThat("IParser result errors should not be empty.", resultHolder.getErrors(), is(not(empty())));
		assertThat("IParser errors should have specified length.", resultHolder.getErrors().size(), is(errorSize));
	}
}