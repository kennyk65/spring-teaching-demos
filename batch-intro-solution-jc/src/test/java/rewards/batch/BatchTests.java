	package rewards.batch;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;

import org.apache.activemq.broker.jmx.QueueViewMBean;
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

	@Test @DirtiesContext  // updates the in-memory database, so dirty the context
	public void runBatch() throws Exception {
		JobExecution execution = testUtils.launchJob();

		assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
		int processed = jdbcTemplate.queryForObject(NR_OF_CONFIRMED_DININGS, Integer.class);
		assertEquals(150, processed);

		assertEquals(150L, diningQueueView.getQueueSize());
	}
}
