package nl.rabobank.statementprocessor.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Validation result, wrapping the validation error list.
 */
@Getter
@Setter
@ToString
public class ValidationResult {
	private List<ValidationError> validationErrors;
	
	public ValidationResult(final List<ValidationError> validationErrors) {
		this.validationErrors = new ArrayList<>();
		
		Optional.ofNullable(validationErrors).ifPresent(this.validationErrors::addAll);
	}
	
	public ValidationResult(final ValidationError validationError) {
		this(Collections.singletonList(validationError));
	}
	
	/**
	 * Returns <code>true</code> if there exists at least one error; <code>false</code> otherwise.
	 * @return <code>true</code> if there exists at least one error; <code>false</code> otherwise.
	 */
	public boolean isValidated() {
		return this.validationErrors.isEmpty();
	}
}
