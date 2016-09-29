package rewards.internal.reward;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.util.Assert;

import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;

import common.datetime.SimpleDate;
import common.money.MonetaryAmount;

/**
 * JDBC implementation of a reward repository that records the result of a
 * reward transaction by inserting a reward confirmation record.
 */
public class JdbcRewardRepository extends JdbcDaoSupport implements RewardRepository {

	@Override
	public RewardConfirmation confirmReward(AccountContribution contribution, Dining dining) {
		String sql = "insert into T_REWARD (CONFIRMATION_NUMBER, DINING_TRANSACTION_ID, REWARD_AMOUNT, REWARD_DATE, ACCOUNT_NUMBER, DINING_MERCHANT_NUMBER, DINING_DATE, DINING_AMOUNT) values (?, ?, ?, ?, ?, ?, ?, ?)";
		String confirmationNumber = nextConfirmationNumber();
		getJdbcTemplate().update(sql, confirmationNumber,
				                 dining.getTransactionId(), contribution.getAmount().asBigDecimal(),
				                 SimpleDate.today().asDate(), contribution.getAccountNumber(),
				                 dining.getMerchantNumber(), dining.getDate().asDate(),
				                 dining.getAmount().asBigDecimal());
		return new RewardConfirmation(confirmationNumber, dining.getTransactionId(), contribution);
	}

	private String nextConfirmationNumber() {
		String sql = "select next value for S_REWARD_CONFIRMATION_NUMBER from DUAL_REWARD_CONFIRMATION_NUMBER";
		return getJdbcTemplate().queryForObject(sql, String.class);
	}

	@Override
	public RewardConfirmation findConfirmationFor(Dining dining) {
		String sql1 = "select CONFIRMATION_NUMBER, DINING_TRANSACTION_ID, "
				    + "ACCOUNT_NUMBER, REWARD_AMOUNT "
				    + "from T_REWARD where DINING_TRANSACTION_ID = ?";
		List<RewardConfirmation> confirmationsOrNull = getJdbcTemplate().query(sql1,
																			   new RewardConfirmationMapper(),
																			   dining.getTransactionId());
		int numberOfConfirmations = confirmationsOrNull.size();
		Assert.state(numberOfConfirmations < 2);
		return numberOfConfirmations == 0 ? null : confirmationsOrNull.get(0);
	}

	private class RewardConfirmationMapper implements RowMapper<RewardConfirmation> {
		@Override
		public RewardConfirmation mapRow(ResultSet rs, int i) throws SQLException {
			return new RewardConfirmation(rs.getString(1), rs.getString(2),
					                      new AccountContribution(rs.getString(3),
																  MonetaryAmount.valueOf(rs.getString(4)),
																  null));
		}
	}

}