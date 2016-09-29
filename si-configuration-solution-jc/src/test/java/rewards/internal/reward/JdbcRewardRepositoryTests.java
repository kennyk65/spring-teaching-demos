package rewards.internal.reward;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.Transactional;

import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.internal.account.Account;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * Tests the JDBC reward repository with a test data source to verify data
 * access and relational-to-object mapping behavior works as expected.
 */
public class JdbcRewardRepositoryTests {

	private JdbcRewardRepository repository;

	private DataSource dataSource;

	@Before
	public void setUp() throws Exception {
		repository = new JdbcRewardRepository();
		dataSource = new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.HSQL)
			.setName("rewards")
			.addScript("db-schema.sql")
			.addScript("db-test-data.sql")
			.build();
		repository.setDataSource(dataSource);
	}

	@Test @Transactional
	public void testCreateReward() throws SQLException {
		Dining dining = Dining.createDining("DININGUUID", "100.00",
				"1234123412341234", "0123456789");

		Account account = new Account("1", "Keith and Keri Donald");
		account.setEntityId(0L);
		account.addBeneficiary("Annabelle", Percentage.valueOf("50%"));
		account.addBeneficiary("Corgan", Percentage.valueOf("50%"));

		AccountContribution contribution = account
				.makeContribution(MonetaryAmount.valueOf("8.00"));
		RewardConfirmation confirmation = repository.confirmReward(
				contribution, dining);

		assertNotNull("confirmation should not be null", confirmation);
		assertNotNull("confirmation number should not be null", confirmation
				.getConfirmationNumber());
		assertEquals("wrong contribution object", contribution, confirmation
				.getAccountContribution());
		verifyRewardInserted(confirmation, dining);
	}

	@Test @Transactional
	public void getExistingRewardConfirmation() throws SQLException {
		Dining dining = Dining.createDining("DININGUUID2", "100.00",
				"1234123412341234", "0123456789");

		Account account = new Account("1", "Keith and Keri Donald");
		account.setEntityId(0L);
		account.addBeneficiary("Annabelle", Percentage.valueOf("50%"));
		account.addBeneficiary("Corgan", Percentage.valueOf("50%"));

		AccountContribution contribution = account
				.makeContribution(MonetaryAmount.valueOf("8.00"));
		RewardConfirmation firstConfirmation = repository.confirmReward(
				contribution, dining);
		RewardConfirmation secondConfirmation = repository.findConfirmationFor(dining);
		assertEquals(firstConfirmation.getDiningTransactionId(), secondConfirmation.getDiningTransactionId());
	}
	@Test @Transactional
	public void getNonExistingRewardConfirmation() throws SQLException {
		Dining dining2 = Dining.createDining("DININGUUID3", "100.00",
				"1234123412341234", "0123456789");
		RewardConfirmation secondConfirmation = repository.findConfirmationFor(dining2);
		assertNull(secondConfirmation);
	}

	private void verifyRewardInserted(RewardConfirmation confirmation,
			Dining dining) throws SQLException {
		assertEquals(1, getRewardCount());
		Statement stmt = dataSource.getConnection().createStatement();
		ResultSet rs = stmt
				.executeQuery("select REWARD_AMOUNT from T_REWARD where CONFIRMATION_NUMBER = '"
						+ confirmation.getConfirmationNumber() + "'");
		rs.next();
		assertEquals(confirmation.getAccountContribution().getAmount(),
				MonetaryAmount.valueOf(rs.getString(1)));
	}

	private int getRewardCount() throws SQLException {
		Statement stmt = dataSource.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery("select count(*) from T_REWARD");
		rs.next();
		return rs.getInt(1);
	}
}