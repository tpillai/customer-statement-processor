package nl.rabobank.statementprocessor.parser;

import nl.rabobank.statementprocessor.entity.Statement;

/**
 * TransactionXmlParser for Statement
 */
public class TransactionXmlParser extends BaseXmlParser<Statement> {
	public TransactionXmlParser() {
		super(Statement.class);
	}
}
