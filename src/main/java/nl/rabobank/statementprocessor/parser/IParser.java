package nl.rabobank.statementprocessor.parser;

/**
 * IParser interface to parse of type <code>T</code> into <code>U</code>.
 * @param <T> Source type.
 * @param <U> Target type.
 */
@FunctionalInterface
public interface IParser<T, U> {
	/**
	 * Function to be performed to parse of type <code>T</code> into <code>U</code>.
	 * @param content content to be parsed.
	 * @return parser result.
	 */
	ResultHolder<U> parse(T content);
}
