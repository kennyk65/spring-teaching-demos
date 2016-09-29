	package rewards.batch;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;

import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.batch.config.SystemTestSelector;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=SystemTestSelector.class)
public class BatchTests {
	static final String NR_OF_CONFIRMED_DININGS = "select count(*) from T_DINING where CONFIRMED=1";
	
	@Autowired JobLauncherTestUtils testUtils;
	@Autowired QueueViewMBean diningQueueView;
	JdbcTemplate jdbcTemplate;

	@Autowired
	public void initJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Test 
	@DirtiesContext  // updates the in-memory database, so dirty the context
	@Ignore
	public void runBatch() throws Exception {
		// TODO 26: Same as 06...
		// TODO 06:	Remove the @Ignore.
		// Use the testUtils variable (defined above) to launch the job.
		// Assert that the resulting JobExecution has an exitStatus of ExitStatus.COMPLETED.
		// Use the jdbcTemplate to assert that the number of confirmed dinings in the database
		// is now 150. (the same as the number of confirmation messages that were on the queue.
		// Note the SQL for the test is defined above.)

		// TODO 32: Same as 12...
		// TODO 12: assert that the batch sent 150 messages using the diningQueueView's queueSize property.
		//	Re-run this test, it should still pass.
	}
}
