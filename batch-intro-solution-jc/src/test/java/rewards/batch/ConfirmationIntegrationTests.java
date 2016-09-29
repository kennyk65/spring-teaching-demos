package rewards.batch;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import rewards.batch.config.SystemTestSelector;

/**
 * Integration test that checks the implementation and configuration 
 * of the ConfirmationReader and -Updater.  
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=SystemTestSelector.class)
public class ConfirmationIntegrationTests {
	
	static final String CONFIRMED_SQL = "select CONFIRMED from T_DINING where ID = ?";

	@Autowired JmsItemReader<Confirmation> confirmationReader;
	@Autowired JdbcBatchItemWriter<Confirmation> confirmationUpdater;
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	void initJdbcTemplate(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional @Test @DirtiesContext
	public void confirm() throws Exception {
		Confirmation confirmation = confirmationReader.read();
		assertNotNull(confirmation);
		String txId = confirmation.getTransactionId();
		
		assertEquals(new Integer(0), jdbcTemplate.queryForObject(CONFIRMED_SQL, Integer.class, txId));
		confirmationUpdater.write(singletonList(confirmation));
		assertEquals(new Integer(1), jdbcTemplate.queryForObject(CONFIRMED_SQL, Integer.class, txId));
	}
}
