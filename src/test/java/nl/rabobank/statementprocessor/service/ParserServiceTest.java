package nl.rabobank.statementprocessor.service;

import nl.rabobank.statementprocessor.configuration.TestConfiguration;
import nl.rabobank.statementprocessor.entity.Statement;
import nl.rabobank.statementprocessor.factory.TestObjectFactory;
import nl.rabobank.statementprocessor.parser.ResultHolder;
import nl.rabobank.statementprocessor.properties.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class ParserServiceTest {
	@Autowired
	@Qualifier("testParserService")
	private ParserService parserService;
	
	@Test
	public void whenCsvTransactionFileIsValidThenParserResultShouldHaveNoErrors() throws IOException {

		String fileContent = TestObjectFactory.getValidCsvFileText();
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.TEXT_CSV, fileContent);
		this.assertValidParserResults(resultHolder);
		

		assertThat("Transaction list should not be empty.", resultHolder.getResult().getTransactions(), is(not(empty())));
		assertThat("Error list should not be empty.", resultHolder.getErrors(), is(not(empty())));
		assertThat("Error list should have exactly specific number of items.", resultHolder.getErrors().size(), is(2));
	}
	
	@Test
	public void whenCsvTransactionFileHasEmptyRecordsThenParserResultShouldHaveNoErrors() throws IOException {

		String fileContent = TestObjectFactory.getEmptyCsvFileText();
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.TEXT_CSV, fileContent);
		this.assertValidParserResults(resultHolder);
		assertThat("Transaction list should be empty.", resultHolder.getResult().getTransactions(), is(empty()));
	}
	
	@Test
	public void whenXmlTransactionFileIsValidThenParserResultShouldHaveNoErrors() throws IOException {

		String fileContent = TestObjectFactory.getValidXmlFileText();
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.APPLICATION_XML, fileContent);
		this.assertValidParserResults(resultHolder);
		assertThat("Transaction list should not be empty.", resultHolder.getResult().getTransactions(), is(not(empty())));
	}
	
	@Test
	public void whenXmlTransactionFileHasEmptyRecordsThenParserResultShouldHaveNoErrors() throws IOException {

		String fileContent = TestObjectFactory.getEmptyXmlFileText();
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.APPLICATION_XML, fileContent);
		this.assertValidParserResults(resultHolder);
		assertThat("Transaction list should be empty.", resultHolder.getResult().getTransactions(), is(not(empty())));
	}
	
	@Test
	public void whenXmlTransactionFileIsInvalidThenParserResultShouldHaveNoErrors() throws IOException {

		String fileContent = TestObjectFactory.getUnparsableXmlFileText();
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.APPLICATION_XML, fileContent);
		this.assertInvalidParserResults(resultHolder, 1);
	}
	
	private <T> void assertValidParserResults(final ResultHolder<T> resultHolder) {
		assertNotNull("Parser result should not be null.", resultHolder);
		assertNotNull("Result object should not be null.", resultHolder.getResult());
	}
	
	private <T> void assertInvalidParserResults(final ResultHolder<T> resultHolder, final int errorSize) {
		assertNotNull("Parser result should not be null.", resultHolder);
		assertThat("Parser result errors should not be empty.", resultHolder.getErrors(), is(not(empty())));
		assertThat("Parser errors should have specified length.", resultHolder.getErrors().size(), is(errorSize));
	}
}