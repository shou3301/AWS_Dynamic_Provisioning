package org.iit.cshou.dp.aws;

import static org.junit.Assert.*;

import org.iit.cshou.dp.client.SleepRequest;
import org.iit.cshou.dp.intl.Request;
import org.junit.Test;

public class QueueServiceTest {

	@Test
	public void testEnqueue() {

		QueueService qs = QueueService.getQueueService();
		
		Request req1 = new SleepRequest("locahost", 1000, "id-1", 10);
		Request req2 = new SleepRequest("locahost", 1000, "id-2", 10);
		Request req3 = new SleepRequest("locahost", 1000, "id-3", 10);
		
		try {
			
			qs.enqueue(req1);
			qs.enqueue(req2);
			qs.enqueue(req3);
			
			Object obj = null;
			
			while ((obj = qs.dequeue()) != null) {
				
				Request tmp = (Request) obj;
				System.out.println(":) Get the result: " + tmp);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
