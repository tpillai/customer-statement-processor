package nl.rabobank.statementprocessor.properties;

import lombok.Getter;
import lombok.Setter;
import nl.rabobank.statementprocessor.factory.YmlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

/**
 * Custom Property Source
 */
@Configuration
@ConfigurationProperties("cs-processor")
@PropertySource(factory = YmlPropertySourceFactory.class, value = ResourceUtils.CLASSPATH_URL_PREFIX + "application.yml")
@Getter
@Setter
public class CustomProperties {

	private Validation validation;

	@Getter
	@Setter
	public static class Validation {

		private String parserErrorsKey ;
	}
}
