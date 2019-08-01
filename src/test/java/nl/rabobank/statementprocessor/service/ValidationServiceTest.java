package nl.rabobank.statementprocessor.service;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import nl.rabobank.statementprocessor.configuration.TestConfiguration;
import nl.rabobank.statementprocessor.factory.TestObjectFactory;
import nl.rabobank.statementprocessor.properties.Constants;
import nl.rabobank.statementprocessor.entity.Statement;
import nl.rabobank.statementprocessor.parser.ResultHolder;
import nl.rabobank.statementprocessor.validator.ValidationResult;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class ValidationServiceTest {
	@Autowired
	@Qualifier("testParserService")
	private ParserService parserService;
	
	@Autowired
	@Qualifier("testValidationService")
	private ValidationService validationService;
	
	@Test
	public void whenCsvTransactionFileIsValidThenValidationResultShouldHaveNoErrors() throws IOException {
		// Given all fields are valid
		String fileContent = TestObjectFactory.getValidCsvFileText();
		
		// Parse file content.
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.TEXT_CSV, fileContent);
		
		// Validate file content.
		ValidationResult validationResult = validationService.validate(resultHolder);
		
		this.assertValidationResults(validationResult);
		
		// Due to erroneous records.
		assertFalse("Validation result should have invalid records.", validationResult.isValidated());
		
		// Due to erroneous records.
		assertThat("Validation result should have exactly specific number of validation errors.", validationResult.getValidationErrors().size(), is(6));
		
		// Should contain transaction #112806 due to uniqueness-violation
		assertThat("Validation errors should contain reference number.",
		           validationResult.getValidationErrors(),
		           hasItem(hasProperty("errorKey", is("112806"))));
		
		assertThat("Validation errors should contain description.",
		           validationResult.getValidationErrors(),
		           hasItem(hasProperty("errorDescription", is("Clothes for Willem Dekker"))));
		
		// Should contain parser error
		assertThat("Validation errors should contain reference number.",
		           validationResult.getValidationErrors(),
		           hasItem(hasProperty("errorKey", is("PARSER_ERROR"))));
	}
	
	@Test
	public void whenCsvTransactionFileHasEmptyRecordsThenValidationResultShouldHaveNoErrors() throws IOException {
		// Given all fields are valid
		String fileContent = TestObjectFactory.getEmptyCsvFileText();
		
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.TEXT_CSV, fileContent);
		
		// Validate file content.
		ValidationResult validationResult = validationService.validate(resultHolder);
		
		this.assertValidationResults(validationResult);
		
		assertTrue("Validation result should not have invalid records.", validationResult.isValidated());
		assertThat("Validation result should not have invalid records.", validationResult.getValidationErrors(), is(empty()));
	}
	
	@Test
	public void whenXmlTransactionFileIsValidThenValidationResultShouldHaveNoErrors() throws IOException {
		// Given all fields are valid
		String fileContent = TestObjectFactory.getValidXmlFileText();
		
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.APPLICATION_XML, fileContent);
		
		// Validate file content.
		ValidationResult validationResult = validationService.validate(resultHolder);
		
		this.assertValidationResults(validationResult);
		
		assertTrue("Validation result should not have invalid records.", validationResult.isValidated());
		assertThat("Validation result should not have invalid records.", validationResult.getValidationErrors(), is(empty()));
	}
	
	@Test
	public void whenXmlTransactionFileHasEmptyRecordsThenValidationResultShouldHaveNoErrors() throws IOException {
		// Given all fields are valid
		String fileContent = TestObjectFactory.getEmptyXmlFileText();
		
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.APPLICATION_XML, fileContent);
		
		// Validate file content.
		ValidationResult validationResult = validationService.validate(resultHolder);
		
		this.assertValidationResults(validationResult);
		
		assertTrue("Validation result should not have invalid records.", validationResult.isValidated());
		assertThat("Validation result should not have invalid records.", validationResult.getValidationErrors(), is(empty()));
	}
	
	@Test
	public void whenXmlTransactionFileIsInvalidThenValidationResultShouldHaveNoErrors() throws IOException {
		// Given all fields are valid
		String fileContent = TestObjectFactory.getUnparsableXmlFileText();
		
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.APPLICATION_XML, fileContent);
		
		// Validate file content.
		ValidationResult validationResult = validationService.validate(resultHolder);
		
		this.assertValidationResults(validationResult);
	}
	
	@Test
	public void whenXmlTransactionFileIsValidWithInvalidRecordsThenValidationResultShouldHaveNoErrors() throws IOException {
		// Given all fields are valid
		String fileContent = TestObjectFactory.getValidXmlFileContentWithInvalidRecords();
		
		// Parse file content.
		ResultHolder<Statement> resultHolder = this.parserService.parseFile(Constants.APPLICATION_XML, fileContent);
		
		// Validate file content.
		ValidationResult validationResult = validationService.validate(resultHolder);
		
		this.assertValidationResults(validationResult);
		
		// Due to erroneous records.
		assertFalse("Validation result should have invalid records.", validationResult.isValidated());
		
		// Due to erroneous records.
		assertThat("Validation result should have exactly specific number of validation errors.", validationResult.getValidationErrors().size(), is(5));
		
		assertThat("Validation errors should contain reference number.",
		           validationResult.getValidationErrors(),
		           hasItem(hasProperty("errorKey", is("147674"))));
		
		assertThat("Validation errors should contain description.",
		           validationResult.getValidationErrors(),
		           hasItem(hasProperty("errorDescription", is("Subscription from Peter Dekker"))));
	}
	
	private void assertValidationResults(final ValidationResult validationResult) {
		assertNotNull("Validation result should not be null.", validationResult);
	}
}