/**
 * 
 */
package org.iit.cshou.dp.scheduler;

import java.util.List;

import org.apache.log4j.Logger;
import org.iit.cshou.dp.aws.QueueService;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;

/**
 * @author cshou
 * 
 */
public class SchedulerDaemon extends Thread {
	
	private Logger log = Logger.getLogger(SchedulerDaemon.class);

	// the name of the original img
	protected static final String IMG_NAME = "ami-3122b501";

	protected QueueService qs = null;

	protected int reqNum = 0;

	protected boolean shutdown = false;

	protected int batchNum = 1;

	protected long interval = 10000;
	
	protected int instanceCount = 0;
	
	protected int threshold = 50;

	public SchedulerDaemon(QueueService qs, long interval, int initInstanceNum,
			int batchNum, int threshold) {
		this.qs = qs;
		this.batchNum = batchNum;
		this.interval = interval;
		this.threshold = threshold;
		
		// launch 1 instance at the beginning
		launchInstances(1);
	}

	protected void launchInstances(int num) {
		
		log.info("Creating " + num + " more instances");

		AmazonEC2 ec2 = new AmazonEC2Client(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		ec2.setRegion(usWest2);

		// change img name as needed
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
				.withInstanceType("t1.micro").withImageId(IMG_NAME).withKeyName("cs553")
				.withMinCount(num).withMaxCount(num).withSecurityGroupIds("cs553");

		RunInstancesResult runInstances = ec2.runInstances(runInstancesRequest);

		// tag each ec2 instances just added
		List<Instance> instances = runInstances.getReservation().getInstances();
		
		for (Instance instance : instances) {
			
			CreateTagsRequest createTagsRequest = new CreateTagsRequest();
			
			createTagsRequest.withResources(instance.getInstanceId())
					.withTags(new Tag("Name", "worker-" + instanceCount));
			
			ec2.createTags(createTagsRequest);

			instanceCount++;
		}
	}

	@Override
	public void run() {

		while (!shutdown) {

			// TODO
			/*
			 * check how many requests in the queue if there is a great change
			 * of number of requests launch new ec2 instances
			 */
			
			int currentSize = qs.size();
			
			log.info("Current SQS size : " + currentSize);
			
			if (currentSize - reqNum > threshold) {
				launchInstances(batchNum);
			}
			
			try {
				sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}

	}

	public void shutdown() {
		this.shutdown = true;
	}

}
