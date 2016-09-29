package rewards.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.listener.SkipListenerSupport;

@SuppressWarnings("rawtypes")
//	TODO 13: Note the implementation of this DiningSkipListener.  
//	TODO 30: Same instructions as 13.  
public class DiningSkipListener extends SkipListenerSupport {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void onSkipInRead(Throwable t) {
		logger.warn("Skipped item because of " + t.getMessage());
	}

}
