package nl.rabobank.statementprocessor.validator;

import nl.rabobank.statementprocessor.entity.Transaction;
import java.math.BigDecimal;
import java.util.Collections;
import javax.validation.constraints.NotNull;
import org.apache.commons.validator.routines.IBANValidator;

/**
 * Validator for transaction class objects.
 */
public class TransactionValidator implements Validator<Transaction> {
	/**
	 * Validates transaction object.
	 *
	 * @param transaction transaction to be validated.
	 *
	 * @return validation result.
	 */
	@Override
	public ValidationResult validate(final @NotNull Transaction transaction) {
		// Validates each field of a transaction.
		// The reason to extend the validation scope onto other fields is to have a better quality validation.
		// If all succeed, return a successful result.
		if (validateTransactionReference(transaction) &&
			validateAccountNumber(transaction) &&
			validateDescription(transaction) &&
			validateStartBalance(transaction) &&
			validateMutation(transaction) &&
			validateEndBalance(transaction)) {
			return new ValidationResult(Collections.emptyList());
		}
		
		// If a validation fails, returns a validation error containing the reference and description.
		return new ValidationResult(new ValidationError(Long.toString(transaction.getTransactionReference()), transaction.getDescription()));
	}
	
	/**
	 * Validates the transaction reference. It needs to be greater than zero.
	 * @param transaction transaction object.
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise.
	 */
	private boolean validateTransactionReference(final @NotNull Transaction transaction) {
		return (transaction.getTransactionReference() > 0);
	}
	
	/**
	 * Validates the account number. It needs to be valid IBAN number according to the Apache IBAN Validator.
	 * @param transaction transaction object.
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise.
	 */
	private boolean validateAccountNumber(final @NotNull Transaction transaction) {
		return IBANValidator.getInstance().isValid(transaction.getAccountNumber());
	}
	
	/**
	 * Validates the description. It is free text, so there is no constraint. The method always returns <code>true</code>.
	 * @param transaction transaction object.
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise.
	 */
	private boolean validateDescription(final @NotNull Transaction transaction) {
		return true;
	}
	
	/**
	 * Validates the starting balance. There is no constraint for this field; therefore, it is always <code>true</code>.
	 * @param transaction transaction object.
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise.
	 */
	private boolean validateStartBalance(final @NotNull Transaction transaction) {
		return true;
	}
	
	/**
	 * Validates the mutation amount. It needs to be non-zero. (This constraint is based on an assumption, not from the assessment.)
	 * @param transaction transaction object.
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise.
	 */
	private boolean validateMutation(final @NotNull Transaction transaction) {
		return (transaction.getMutation().compareTo(BigDecimal.ZERO) != 0);
	}
	
	/**
	 * Validates the ending balance. It needs to be equal to the sum of starting balance amount and the mutation amount.
	 * @param transaction transaction object.
	 * @return <code>true</code> if the field is valid, <code>false</code> otherwise.
	 */
	private boolean validateEndBalance(final @NotNull Transaction transaction) {
		return (transaction.getEndBalance().compareTo(transaction.getStartBalance().add(transaction.getMutation())) == 0);
	}
}
