package rewards.batch;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

public class DiningMapper implements RowMapper<Dining> {
	private Log logger = LogFactory.getLog(getClass());

	@Override
	public Dining mapRow(ResultSet rs, int rowNum) throws SQLException {
		String transactionId = rs.getString("ID");
		String creditCardNumber = rs.getString("CREDIT_CARD_NUMBER");
		String merchantNumber = rs.getString("MERCHANT_NUMBER");
		BigDecimal amount = rs.getBigDecimal("AMOUNT");
		Date date = rs.getDate("DINING_DATE");
		boolean confirmed = rs.getBoolean("CONFIRMED");
		logger.debug("Found Dining with transactionId " + transactionId);
		return new Dining(transactionId, amount, creditCardNumber, merchantNumber, date, confirmed);
	}

}
