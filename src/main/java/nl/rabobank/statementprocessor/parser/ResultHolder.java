package nl.rabobank.statementprocessor.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * ResultHolder holds the parsed results
 *
 * @param <T> result type.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ResultHolder<T> {
	private T result;
	private List<String> errors;

	public ResultHolder() {
		this.errors = new ArrayList<>();
	}

	public ResultHolder(final T result) {
		this();
		this.result = result;
	}
}