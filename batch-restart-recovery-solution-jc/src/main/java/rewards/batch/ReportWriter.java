package rewards.batch;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;

import rewards.RewardConfirmation;

public class ReportWriter implements ItemWriter<RewardConfirmation> {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void write(List<? extends RewardConfirmation> items) throws Exception {
		logger.debug("wrote " + items.size() 
				+ " confirmations, last confirmation nr = " 
				+ items.get(items.size() - 1).getConfirmationNumber());
	}

}
