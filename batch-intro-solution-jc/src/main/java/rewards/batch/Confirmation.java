package rewards.batch;

import java.io.Serializable;

public class Confirmation implements Serializable {

	private static final long serialVersionUID = -7657086189711480213L;
	private final String transactionId;

	public Confirmation(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionId() {
		return transactionId;
	}
}
