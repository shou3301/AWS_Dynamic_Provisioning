/**
 * 
 */
package org.iit.cshou.dp.worker;

import java.util.concurrent.atomic.AtomicInteger;

import org.iit.cshou.dp.intl.Request;

/**
 * @author cshou
 *
 */
public class WorkerLeader extends Thread {

	protected long interval = 1;
	
	protected boolean stop = false;
	
	protected AtomicInteger counter = null;
	
	protected int threshold = 1;
	
	protected int batchNum = 1;
	
	// only for local test
	// TODO remove later
	protected TestQueue queue = null;
	
	/*
	 * TODO 
	 * there should be a reference to SQS here
	 */
	
	/**
	 * @param interval the time interval to pull jobs from the queue
	 * @param threshold the max number of jobs can be run within the current worker leader
	 * @param batchNum the number of jobs to pull each time
	 */
	public WorkerLeader (long interval, int threshold, int batchNum) {
		
		this.interval = interval;
		this.threshold = threshold;
		this.batchNum = batchNum;
		
		queue = TestQueue.getQueue();
		
		counter = new AtomicInteger();
		
	}
	
	@Override
	public void run () {
		
		while (!stop) {
			
			if (!counter.compareAndSet(threshold, threshold)) {
				
				int i = 0;
				
				while (!counter.compareAndSet(threshold, threshold) && 
						i < this.batchNum) {
					
					if (!queue.empty()) {
						
						Request req = queue.dequeue();
						new Thread(new Worker(req, this)).run();
						
					}
					
				}
				
			}
			
			try {
				
				sleep(interval);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void notifyFinish () {
		this.counter.getAndDecrement();
	}
	
	public void notifyStart () {
		this.counter.getAndIncrement();
	}
	
}
