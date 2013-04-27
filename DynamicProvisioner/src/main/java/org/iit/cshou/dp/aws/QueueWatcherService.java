/**
 * 
 */
package org.iit.cshou.dp.aws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.Metric;

/**
 * @author cshou
 *
 */
public class QueueWatcherService {

	protected static QueueWatcherService watcher = null;
	
	protected AmazonCloudWatch awsCloudWatch = null;
	
	private QueueWatcherService () {
		
		awsCloudWatch = new AmazonCloudWatchClient(new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		awsCloudWatch.setRegion(usWest2);
		
		
	}
	
	public static QueueWatcherService getWatcherService () {
		
		if (watcher == null)
			watcher = new QueueWatcherService();
		
		return watcher;
	}
	
	public List<Metric> getAllMetrics () throws Exception {
		
		return awsCloudWatch.listMetrics().getMetrics();
		
	}
	
	public Metric getQueueMetric (String queueName, String metricName) 
		throws Exception {
		
		List<Metric> all = awsCloudWatch.listMetrics().getMetrics();
		
		for (Metric mt : all) {
			
			if (queueName.equalsIgnoreCase(mt.getDimensions().get(0).getValue()) &&
					metricName.equalsIgnoreCase(mt.getMetricName())) {
				
				return mt;
				
			}
			
		}
		
		return null;
		
	}
	
	public GetMetricStatisticsResult getStatistics (String namespace, String queueName, String metricName) 
		throws Exception {
		
		GetMetricStatisticsRequest getMetricStatisticsRequest = new GetMetricStatisticsRequest();
		getMetricStatisticsRequest.setNamespace(namespace);
		getMetricStatisticsRequest.setMetricName(metricName);
		
		List<Dimension> dimensions = new ArrayList<Dimension>();
		Dimension d = new Dimension();
		d.setName("QueueName");
		d.setValue(queueName);
		dimensions.add(d);
		
		getMetricStatisticsRequest.setDimensions(dimensions);
		
		getMetricStatisticsRequest.setEndTime(new Date());
		getMetricStatisticsRequest.setPeriod(60);
		getMetricStatisticsRequest.setStartTime(new Date(new Date().getTime() - 3600));
		
		List<String> statistics = new ArrayList<String>();
		statistics.add("Sum");
		statistics.add("Maximum");
		statistics.add("Minimum");
		statistics.add("SampleCount");
		statistics.add("Average");
		getMetricStatisticsRequest.setStatistics(statistics);
		
		GetMetricStatisticsResult result = awsCloudWatch.getMetricStatistics(getMetricStatisticsRequest);
		
		return result;
	}
	
	public GetMetricStatisticsResult getStatistics (Metric metric) {
		GetMetricStatisticsRequest getMetricStatisticsRequest = new GetMetricStatisticsRequest();
		getMetricStatisticsRequest.setEndTime(new Date());
		getMetricStatisticsRequest.setPeriod(60);
		getMetricStatisticsRequest.setStartTime(new Date(new Date().getTime() - 3600));
		getMetricStatisticsRequest.setDimensions(metric.getDimensions());
		getMetricStatisticsRequest.setMetricName(metric.getMetricName());
		getMetricStatisticsRequest.setNamespace(metric.getNamespace());
		List<String> statistics = new ArrayList<String>();
		statistics.add("Sum");
		statistics.add("Maximum");
		statistics.add("Minimum");
		statistics.add("SampleCount");
		statistics.add("Average");
		getMetricStatisticsRequest.setStatistics(statistics);
		
		GetMetricStatisticsResult result = awsCloudWatch.getMetricStatistics(getMetricStatisticsRequest);
		
		return result;
	}

}
