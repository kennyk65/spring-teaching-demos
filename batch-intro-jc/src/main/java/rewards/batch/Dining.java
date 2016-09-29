package rewards.batch;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Dining implements Serializable {

	private static final long serialVersionUID = 3968043929818855481L;
	private final String transactionId;
	private final BigDecimal amount;
	private final String creditCardNumber;
	private final String merchantNumber;
	private final Date date;
	private boolean confirmed = false;

	/**
	 * Creates a new Dining with a new transactionId and date set to now
	 * @param amount
	 * @param creditCardNumber
	 */
	public Dining(BigDecimal amount, String creditCardNumber, String merchantNumber) {
		this(UUID.randomUUID().toString(), amount, creditCardNumber, merchantNumber, new Date(), false);
	}

	/**
	 * Fully specifies the state of the created Dining, for use by Repositories
	 * @param transactionId
	 * @param amount
	 * @param creditCardNumber
	 * @param merchantNumber
	 * @param date
	 * @param confirmed
	 */
	public Dining(String transactionId, BigDecimal amount, String creditCardNumber, String merchantNumber,
			Date date, boolean confirmed) {
		this.transactionId = transactionId;
		this.amount = amount;
		this.creditCardNumber = creditCardNumber;
		this.merchantNumber = merchantNumber;
		this.date = date;
		this.confirmed = confirmed;
	}

	public String getTransactionId() {
		return transactionId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public String getMerchantNumber() {
		return merchantNumber;
	}
	public Date getDate() {
		return date;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void confirm() {
		this.confirmed = true;
	}

}
