package nl.rabobank.statementprocessor.factory;

import nl.rabobank.statementprocessor.entity.Transaction;
import nl.rabobank.statementprocessor.entity.Statement;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ResourceUtils;

public final class TestObjectFactory {
	private TestObjectFactory() {
	}
	
	public static Transaction createValidTransaction() {
		Transaction transaction = new Transaction();
		
		transaction.setTransactionReference(123456L);
		transaction.setAccountNumber("NL43AEGO0773393871");
		transaction.setDescription("Transaction description");
		transaction.setStartBalance(BigDecimal.valueOf(100));
		transaction.setMutation(BigDecimal.valueOf(25));
		transaction.setEndBalance(BigDecimal.valueOf(125));
		
		return transaction;
	}
	
	public static Statement createValidStatement() {
		Statement statement = new Statement();
		
		Transaction transaction1 = createValidTransaction();
		transaction1.setDescription("Transaction description 1");
		
		Transaction transaction2 = createValidTransaction();
		transaction2.setTransactionReference(98765L);
		transaction2.setDescription("Transaction description 2");
		
		Transaction transaction3 = createValidTransaction();
		transaction3.setTransactionReference(581923L);
		transaction3.setDescription("Transaction description 3");
		
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction1);
		transactions.add(transaction2);
		transactions.add(transaction3);
		
		statement.setTransactions(transactions);
		
		return statement;
	}
	
	public static String getUnparsableXmlFileText() throws IOException {
		return String.join(System.lineSeparator(), Files.readAllLines(ResourceUtils.getFile("classpath:data/recordsUnparsable.xml").toPath()));
	}
	
	public static String getEmptyXmlFileText() throws IOException {
		return String.join(System.lineSeparator(), Files.readAllLines(ResourceUtils.getFile("classpath:data/recordsEmpty.xml").toPath()));
	}
	
	public static String getValidXmlFileText() throws IOException {
		return String.join(System.lineSeparator(), Files.readAllLines(ResourceUtils.getFile("classpath:data/recordsValid.xml").toPath()));
	}
	
	public static String getValidXmlFileContentWithInvalidRecords() throws IOException {
		return String.join(System.lineSeparator(), Files.readAllLines(ResourceUtils.getFile("classpath:data/recordsWithInvalid.xml").toPath()));
	}
	
	public static String getEmptyCsvFileText() throws IOException {
		return String.join(System.lineSeparator(), Files.readAllLines(ResourceUtils.getFile("classpath:data/recordsEmpty.csv").toPath()));
	}
	
	public static String getValidCsvFileText() throws IOException {
		return String.join(System.lineSeparator(), Files.readAllLines(ResourceUtils.getFile("classpath:data/recordsValid.csv").toPath()));
	}
}
