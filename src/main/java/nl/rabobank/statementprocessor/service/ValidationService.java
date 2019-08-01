package nl.rabobank.statementprocessor.service;

import nl.rabobank.statementprocessor.entity.Statement;
import nl.rabobank.statementprocessor.parser.ResultHolder;
import nl.rabobank.statementprocessor.properties.CustomProperties;
import nl.rabobank.statementprocessor.properties.CustomProperties.Validation;
import nl.rabobank.statementprocessor.validator.StatementValidator;
import nl.rabobank.statementprocessor.validator.ValidationError;
import nl.rabobank.statementprocessor.validator.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 *  This class represents the Validation service to validate statements
 */
@Service
public class ValidationService {
	private final StatementValidator validator;
	private final Validation validationProperties;
	
	@Autowired
	public ValidationService(final StatementValidator validator, final CustomProperties customProperties) {
		this.validator = validator;
		this.validationProperties = customProperties.getValidation();

	}
	
	/**
	 * Validates the  Statement.
	 *
	 * @param resultHolder parse result from  file  and mapped to statement
	 * @return validation result.
	 */
	public ValidationResult validate(final ResultHolder<Statement> resultHolder) {

		ValidationResult validationResult = Optional.ofNullable(resultHolder.getResult())
		                                            .map(this.validator::validate)
		                                            .orElse(new ValidationResult(
		                                            		new ValidationError(validationProperties.getParserErrorsKey(), "Failed to parse Statement.")
													));
		
			Optional.ofNullable(resultHolder.getErrors())
			        .orElse(Collections.emptyList())
			        .forEach(error -> validationResult.getValidationErrors().add(
			        		new ValidationError(validationProperties.getParserErrorsKey(), error))
					);
		

		return validationResult;
	}
}
