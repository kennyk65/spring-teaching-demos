package rewards.batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

/**
 * This class supports the JdbcBatchItemWriter by defining how the parameters
 * within the SQL statement should be set using the incoming Confirmation
 * object.
 */
public class ConfirmationPreparedStatementPreparer implements
		ItemPreparedStatementSetter<Confirmation> {

	@Override
	public void setValues(Confirmation item, PreparedStatement ps) throws SQLException {
		ps.setString(1, item.getTransactionId());
	}

}