package nl.rabobank.statementprocessor.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.rabobank.statementprocessor.entity.Statement;
import nl.rabobank.statementprocessor.parser.ResultHolder;
import nl.rabobank.statementprocessor.service.ParserService;
import nl.rabobank.statementprocessor.service.ValidationService;
import nl.rabobank.statementprocessor.validator.ValidationResult;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Statement Processor Controller
 */
@RestController
@Api(value = "/")
public class StatementProcessorController {
	private static final Logger logger = LoggerFactory.getLogger(StatementProcessorController.class);
	
	private final ParserService parserService;
	private final ValidationService validationService;
	
	@Autowired
	public StatementProcessorController(final ParserService parserService, final ValidationService validationService) {
		this.parserService = parserService;
		this.validationService = validationService;
	}
	
	/**
	 * Validates the statement file.
	 *
	 * @param statementFile statement records file.
	 *
	 * @return validation result of the file content.
	 */
	@PostMapping("/processFile")
	@ApiOperation(value = "/processFile", httpMethod = "POST")
	public ResponseEntity<ValidationResult> processStatementFile(@RequestParam("file") final MultipartFile statementFile) {
		if ((statementFile == null) || statementFile.isEmpty()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		String contentType = statementFile.getContentType();
		if (!parserService.isParsable(contentType)) {
			return ResponseEntity.unprocessableEntity().build();
		}
		String content;
		try (InputStream inputStream = statementFile.getResource().getInputStream()) {
			content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		} catch (IOException ioe) {
			String errorMessage = "Failed to read file content.";
			logger.error(errorMessage, ioe);
			return ResponseEntity.unprocessableEntity().build();
		}

		ResultHolder<Statement> resultHolder = parserService.parseFile(contentType, content);
		ValidationResult validationResult = validationService.validate(resultHolder);
		return ResponseEntity.ok(validationResult);
	}
}
