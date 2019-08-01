package nl.rabobank.statementprocessor.factory;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * Factory to parse YAML property sources.
 */
public class YmlPropertySourceFactory implements PropertySourceFactory {
    /**
     * Creates property source from YAML resource.
     *
     * @param name YAML resource name.
     * @param resource YAML resource.
     * @return property source constructed from YAML resource.
     * @throws IOException when an I/O exception occurs while reading / parsing the source resource.
     */
    @Override
    public PropertySource<?> createPropertySource(@Nullable final String name, final EncodedResource resource) throws IOException {

        Properties propertiesFromYaml = loadYamlIntoProperties(resource);
        String sourceName = Optional.ofNullable(name).orElseGet(() -> resource.getResource().getFilename());
        return new PropertiesPropertySource(sourceName, propertiesFromYaml);
    }
    
    /**
     * Builds properties resource from YAML resource.
     *
     * @param resource YAML resource.
     * @return properties resource.
     * @throws FileNotFoundException occurs when file with the specified resource does not exist.
     */
    private Properties loadYamlIntoProperties(final EncodedResource resource) throws FileNotFoundException {
        try {

            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (IllegalStateException e) {

            Throwable cause = e.getCause();
            if (cause instanceof FileNotFoundException)
                throw (FileNotFoundException) e.getCause();
            throw e;
        }
    }
}