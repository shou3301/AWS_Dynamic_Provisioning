package org.iit.cshou.dp.aws;

import static org.junit.Assert.*;

import org.junit.Test;

import com.amazonaws.services.cloudwatch.model.Metric;

public class WatcherServiceTest {

	@Test
	public void testGetAllMetrics() throws Exception {

		QueueWatcherService ws = QueueWatcherService.getWatcherService();
		System.out.println(ws.getAllMetrics());
		
	}
	
	@Test
	public void testGetQueueMetric() throws Exception {
		
		QueueWatcherService ws = QueueWatcherService.getWatcherService();
		Metric m = ws.getQueueMetric("manual-test-1", "SentMessageSize");
		System.out.println(m);
	}
	
	@Test
	public void testGetStatistics () throws Exception {
		
		QueueWatcherService ws = QueueWatcherService.getWatcherService();
		// System.out.println(ws.getStatistics("AWS/SQS", "manual-test-1", "SentMessageSize"));
		System.out.println(ws.getStatistics(ws.getQueueMetric("manual-test-1", "SentMessageSize")));
		
	}

}
