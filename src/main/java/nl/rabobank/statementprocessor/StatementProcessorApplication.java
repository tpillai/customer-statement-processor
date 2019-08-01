package nl.rabobank.statementprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main entry for the application.
 *
 * Enables configuration properties with annotation.
 */
@SpringBootApplication
@EnableConfigurationProperties
public class StatementProcessorApplication {
	public static void main(String[] args) {
		SpringApplication.run(StatementProcessorApplication.class, args);
	}
}

