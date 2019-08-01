package nl.rabobank.statementprocessor.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 *  This class represents Transaction
 */
@Getter
@Setter
@JacksonXmlRootElement(localName = "record")
@ToString
public class Transaction {
	@JacksonXmlProperty(localName = "reference", isAttribute = true) private long transactionReference;
	@JacksonXmlProperty(localName = "accountNumber") private String accountNumber;
	@JacksonXmlProperty(localName = "description") private String description;
	@JacksonXmlProperty(localName = "startBalance") private BigDecimal startBalance;
	@JacksonXmlProperty(localName = "mutation") private BigDecimal mutation;
	@JacksonXmlProperty(localName = "endBalance") private BigDecimal endBalance;



}
