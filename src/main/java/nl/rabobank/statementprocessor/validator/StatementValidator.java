package nl.rabobank.statementprocessor.validator;

import nl.rabobank.statementprocessor.entity.Transaction;
import nl.rabobank.statementprocessor.entity.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 * Validator for Statement.
 */
public class StatementValidator implements Validator<Statement> {
	private final TransactionValidator transactionValidator;
	
	public StatementValidator() {
		super();
		
		this.transactionValidator = new TransactionValidator();
	}
	
	/**
	 * Validates the transaction bucket, by validating each transaction within.
	 *
	 * @param statement transaction bucket to validate.
	 *
	 * @return validation result.
	 */
	@Override
	public ValidationResult validate(final @NotNull Statement statement) {

		List<ValidationError> errorList = new ArrayList<>();
		
		// If transactions is not null; then loops through the transactions. Then
		// 1. Groups all the transactions by the reference.
		// 2. Performs the operation for each transactions grouped by the transaction reference.
		Optional.ofNullable(statement.getTransactions())
		        .orElse(Collections.emptyList())
		        .stream()
		        .collect(Collectors.groupingBy(Transaction::getTransactionReference))
		        .forEach((transactionReference, transactionList) -> {
		        	if (transactionList.size() > 1) {
						// If there are more than 1 (one) transactions for a transaction, then it breaks the constraint of 'Unique Reference Number'.
						// Therefore, every transaction should be added to error list, without even validating.
						transactionList.forEach(o -> errorList.add(new ValidationError(Long.toString(transactionReference), o.getDescription())));
					} else {
						// Otherwise, meaning that there is exactly 1 (one) transaction for the reference number,
						// Append and validation error for the specific transaction.
						errorList.addAll(this.transactionValidator.validate(transactionList.get(0)).getValidationErrors());
					}
		        });
		
		// Returns a new validation result containing the error list.
		return new ValidationResult(errorList);
	}
}
