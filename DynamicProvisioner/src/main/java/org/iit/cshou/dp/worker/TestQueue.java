/**
 * 
 */
package org.iit.cshou.dp.worker;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.iit.cshou.dp.intl.Request;

/**
 * @author cshou
 * Just a test queue that can be shared by multiple workers
 */
public class TestQueue {

	private static TestQueue queue = null;
	
	private ConcurrentLinkedQueue<Request> store = null;
	
	private TestQueue () {
		store = new ConcurrentLinkedQueue<Request>();
	}
	
	public static synchronized TestQueue getQueue () {
		if (queue == null)
			queue = new TestQueue();
		return queue;
	}
	
	public void enqueue (Request t) {
		store.add(t);
	}
	
	public Request dequeue () {
		return store.remove();
	}
	
	public boolean empty() {
		return store.peek() == null ? true : false;
	}
	
}
