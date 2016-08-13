package com.example;

import org.springframework.stereotype.Service;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;

/**
 * All of the CloudWatch specific logic is in this service.
 */
@Service
public class MonitoringService {

	private AmazonCloudWatchAsyncClient cloudWatch = new AmazonCloudWatchAsyncClient();
	
	/**
	 * Log the current level of student Boredom.  
	 * No pre-registration of custom metric is needed!  
	 * See results at https://console.aws.amazon.com/cloudwatch/home?region=us-east-1#metrics:graph=~(metrics~(~(~'Training~'BoredomLevel))~period~900~stat~'Average~start~'-PT1H~end~'P0D~yAxis~(left~null~right~null)~region~'us-east-1)
	 * Warning - May take up to 15 minutes for new custom metric to appear.
	 */
	public void logBoredomLevel(long level) {
		
		PutMetricDataRequest request = new PutMetricDataRequest()
			.withNamespace("Training")
			.withMetricData(
				new MetricDatum()
					.withMetricName("BoredomLevel")
					.withValue(new Double(level)));
		
		cloudWatch.putMetricDataAsync(request);	// Yup, it's async!
	}
}
