package nl.rabobank.statementprocessor.validator;

/**
 * Validator interface to validate type <code>T</code>
 * @param <T> generic type to be validated.
 */
@FunctionalInterface
public interface Validator<T> {
	/**
	 * Validates the content.
	 *
	 * @param content content of type <code>T</code>.
	 *
	 * @return validation result after validating.
	 */
	ValidationResult validate(T content);
}
