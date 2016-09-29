package rewards;

import java.io.Serializable;

import org.springframework.util.Assert;

/**
 * A summary of a confirmed reward transaction describing a contribution made to
 * an account that was distributed among the account's beneficiaries.
 */
@SuppressWarnings("serial")
public class RewardConfirmation implements Serializable {

	private final String confirmationNumber;

	private final AccountContribution accountContribution;

	private final String diningTransactionId;

	/**
	 * Creates a new reward confirmation.
	 * 
	 * @param confirmationNumber
	 *            the unique confirmation number
	 * @param accountContribution
	 *            a summary of the account contribution that was made
	 */
	public RewardConfirmation(String confirmationNumber, String diningTransactionId,
			AccountContribution accountContribution) {
		Assert.hasText(confirmationNumber, "'confirmationNumber' must have text");
		Assert.hasText(diningTransactionId, "'diningTransactionId' must have text");
		Assert.notNull(accountContribution, "'accountContribution' must not be null");
		this.confirmationNumber = confirmationNumber;
		this.diningTransactionId = diningTransactionId;
		this.accountContribution = accountContribution;
	}

	/**
	 * Returns the confirmation number of the reward transaction. Can be used
	 * later to lookup the transaction record.
	 */
	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	/**
	 * Returns a summary of the monetary contribution that was made to an
	 * account.
	 * 
	 * @return the account contribution (the details of this reward)
	 */
	public AccountContribution getAccountContribution() {
		return accountContribution;
	}

	public String getDiningTransactionId() {
		return diningTransactionId;
	}

	@Override
	public String toString() {
		return String
				.format(
						"Reward of %s applied to account %s. RewardConfirmation id is: %s",
						accountContribution.getAmount(), accountContribution
								.getAccountNumber(), confirmationNumber);
	}

	@Override
	public int hashCode() {
		return accountContribution.hashCode() + confirmationNumber.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof RewardConfirmation)) {
			return false;
		}
		RewardConfirmation other = (RewardConfirmation) o;
		// value objects are equal if their attributes are equal
		return accountContribution.equals(other.accountContribution)
				&& confirmationNumber.equals(other.confirmationNumber);
	}
}