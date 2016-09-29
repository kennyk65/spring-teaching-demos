package rewards.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.batch.config.SystemTestSelector;

/**
 * Simple integration test that checks if the unconfirmedDiningsReader
 * has been correctly configured as a Spring bean to help with the lab. 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=SystemTestSelector.class)
public class UnconfirmedDiningsReaderTests {
	@Autowired JdbcPagingItemReader<Dining> diningsReader;
	
	@Test 
	@DirtiesContext
	@Ignore
	//	TODO 09: Remove the @Ignore and run this integration test, it should pass.
	//	TODO 29: Same as 09...
	public void readUnconfirmedDinings() throws Exception {
		Dining dining = diningsReader.read();
		assertNotNull(dining);
		assertEquals("127cc1d1-cb90-4810-b373-0c66068e3000", dining.getTransactionId());
	}
}
