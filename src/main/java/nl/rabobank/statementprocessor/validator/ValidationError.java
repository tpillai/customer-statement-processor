package nl.rabobank.statementprocessor.validator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Validation Error class.
 */
@Getter
@AllArgsConstructor
@ToString
public class ValidationError {
	private String errorKey;
	private String errorDescription;
}
