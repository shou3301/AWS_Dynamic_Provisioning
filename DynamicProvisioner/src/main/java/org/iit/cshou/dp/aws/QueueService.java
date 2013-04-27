/**
 * 
 */
package org.iit.cshou.dp.aws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.iit.cshou.dp.util.ObjectCoder;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

/**
 * @author cshou
 *
 */
public class QueueService {
	
	private Logger log = Logger.getLogger(QueueService.class);
	
	private static final String QUEUE_NAME = "test-queue-1";

	protected static QueueService queueService = null;
	
	protected AmazonSQS sqs = null;
	
	protected String sqsUrl = null;
	
	private QueueService () {
		
		sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		sqs.setRegion(usWest2);
		
		log.info("Creating a SQS queue for push requests.");
		CreateQueueRequest createQueueRequest = new CreateQueueRequest(QUEUE_NAME);
        sqsUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		
	}
	
	private QueueService (String queueName) {
		
		sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		sqs.setRegion(usWest2);
		
		GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
		GetQueueUrlResult getQueueUrlResult = sqs.getQueueUrl(getQueueUrlRequest);
		sqsUrl = getQueueUrlResult.getQueueUrl();
		
	}
	
	/**
	 * @return create a new queue service
	 */
	public static QueueService createQueueService () {
		
		if (queueService == null)
			queueService = new QueueService();
		
		return queueService;
		
	}
	
	/**
	 * @param queueName
	 * @return get an existing queue service by name
	 */
	public static QueueService getQueueService (String queueName) {
		
		if (queueService == null)
			queueService = new QueueService(queueName);
		
		return queueService;
		
	}
	
	public void enqueue (Object object) throws Exception {
		
		log.info("Sending object to SQS");
		
		String coded = ObjectCoder.code(object);
		this.sqs.sendMessage(new SendMessageRequest(this.sqsUrl, coded));
		
	}
	
	public int size () {
		
		log.info("Getting SQS size");
		
		GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest();
		getQueueAttributesRequest.setQueueUrl(sqsUrl);
		//getQueueAttributesRequest.setRequestCredentials((AWSCredentials) new ClasspathPropertiesFileCredentialsProvider());
		
		ArrayList<String> attr = new ArrayList<String>();
		attr.add("ApproximateNumberOfMessages");
		getQueueAttributesRequest.setAttributeNames(attr);
		
		// log.info("attr request: " + getQueueAttributesRequest);
		
		GetQueueAttributesResult getQueueAttributesResult = sqs.getQueueAttributes(getQueueAttributesRequest);
		
		// log.info("attr result: " + getQueueAttributesResult);
		int size = Integer.parseInt(getQueueAttributesResult.getAttributes().get("ApproximateNumberOfMessages"));
		
		return size;
	}
	
	public Object dequeue () throws Exception {
		
		log.info("Getting object from SQS");
		
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl);
		receiveMessageRequest.setMaxNumberOfMessages(1);
		receiveMessageRequest.setVisibilityTimeout(0);
		
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		
		if (messages.size() == 0)
			return null;
		
		Message single = messages.get(0);
		
		log.info("Message returned: [" + single.getMessageId() + "] [" +
				single.getMD5OfBody() + "]");
		
		String codeString = single.getBody();
		
		Object result = ObjectCoder.decode(codeString);
		
		String messageRecieptHandle = single.getReceiptHandle();
		sqs.deleteMessage(new DeleteMessageRequest(sqsUrl, messageRecieptHandle));
		
		return result;
	}
	
}
