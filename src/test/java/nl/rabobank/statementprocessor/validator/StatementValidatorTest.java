package nl.rabobank.statementprocessor.validator;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import nl.rabobank.statementprocessor.factory.TestObjectFactory;
import nl.rabobank.statementprocessor.entity.Transaction;
import nl.rabobank.statementprocessor.entity.Statement;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class StatementValidatorTest {
	private StatementValidator validator;
	
	@Before
	public void setUp() {
		this.validator = new StatementValidator();
	}
	
	@Test
	public void whenTransactionFieldsAreProperThenValidationResultShouldHaveNoErrors() {
		// Given all fields are valid
		Statement statement = TestObjectFactory.createValidStatement();
		
		ValidationResult validationResult = this.validator.validate(statement);
		this.assertValidStatementValidationResults(validationResult);
	}
	
	@Test
	public void whenAccountNumberIsNotValidForATransactionThenValidationResultShouldHaveErrors() {
		// Given all fields are valid
		Statement statement = TestObjectFactory.createValidStatement();
		
		// Invalidating account number
		Transaction invalidTransaction = statement.getTransactions().get(0);
		invalidTransaction.setAccountNumber("NNNNN31232NNN02");
		
		ValidationResult validationResult = this.validator.validate(statement);
		this.assertInvalidStatementValidationResults(validationResult, 1);
		this.assertInvalidTransactionShouldExistInValidationResults(invalidTransaction, validationResult);
	}
	
	@Test
	public void whenEndBalanceIsNotValidForAtransactionThenValidationResultShouldHaveErrors() {
		// Given all fields are valid
		Statement statement = TestObjectFactory.createValidStatement();
		
		// Invalidating end balance, having `end-balance = start-balance + mutation + 10`
		Transaction invalidTransaction = statement.getTransactions().get(1);
		invalidTransaction.setEndBalance(invalidTransaction.getStartBalance().add(invalidTransaction.getMutation()).add(BigDecimal.TEN));
		
		ValidationResult validationResult = this.validator.validate(statement);
		this.assertInvalidStatementValidationResults(validationResult, 1);
		this.assertInvalidTransactionShouldExistInValidationResults(invalidTransaction, validationResult);
	}
	
	@Test
	public void whenReferenceNumberIsNotUniqueThenValidationResultShouldHaveErrors() {
		// Given all fields are valid
		Statement statement = TestObjectFactory.createValidStatement();
		
		// Duplicating reference number
		Transaction invalidTransaction1 = statement.getTransactions().get(0);
		invalidTransaction1.setTransactionReference(123456L);
		
		// Duplicating reference number
		Transaction invalidTransaction2 = statement.getTransactions().get(1);
		invalidTransaction2.setTransactionReference(123456L);
		
		ValidationResult validationResult = this.validator.validate(statement);
		this.assertInvalidStatementValidationResults(validationResult, 2);
		this.assertInvalidTransactionShouldExistInValidationResults(invalidTransaction1, validationResult);
		this.assertInvalidTransactionShouldExistInValidationResults(invalidTransaction2, validationResult);
	}
	
	private void assertValidStatementValidationResults(final ValidationResult validationResult) {
		assertNotNull("Validation result should not be null.", validationResult);
		assertTrue("Validation result should be validated.", validationResult.isValidated());
		assertThat("Validation errors should be empty.", validationResult.getValidationErrors(), is(empty()));
	}
	
	private void assertInvalidStatementValidationResults(final ValidationResult validationResult, final int errorSize) {
		assertNotNull("Validation result should not be null.", validationResult);
		assertFalse("Validation result should not be validated.", validationResult.isValidated());
		assertThat("Validation errors should not be empty.", validationResult.getValidationErrors(), is(not(empty())));
		assertThat("Validation errors should have specified length.", validationResult.getValidationErrors().size(), is(errorSize));
	}
	
	private void assertInvalidTransactionShouldExistInValidationResults(final Transaction transaction, final ValidationResult validationResult) {
		assertThat("Validation errors should contain reference number.",
		           validationResult.getValidationErrors(),
		           hasItem(hasProperty("errorKey", is(Long.toString(transaction.getTransactionReference())))));
		assertThat("Validation errors should contain description.",
		           validationResult.getValidationErrors(),
		           hasItem(hasProperty("errorDescription", is(transaction.getDescription()))));
	}
}