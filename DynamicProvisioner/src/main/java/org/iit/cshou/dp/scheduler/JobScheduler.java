/**
 * 
 */
package org.iit.cshou.dp.scheduler;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.log4j.Logger;
import org.iit.cshou.dp.client.SimpleClient;
import org.iit.cshou.dp.intl.Request;
import org.iit.cshou.dp.intl.RequestHandler;
import org.iit.cshou.dp.worker.TestQueue;

/**
 * @author cshou
 *
 */
public class JobScheduler implements Runnable {
	
	private static Logger log = Logger.getLogger(JobScheduler.class);
	
	protected static final int REG_PORT = 6667;
	
	protected int svcPort = -1;
	
	/*
	 * TODO 
	 * create SQS and CloudWatch
	 * create a daemon, and pass in SQS and CloudWatch to monitor
	 * 
	 */
	
	// TODO only for test, to be removed later
	protected TestQueue queue = null;
	
	public JobScheduler (int svcPort) throws Exception {
		
		this.svcPort = svcPort;
		
		queue = TestQueue.getQueue();
		
		// register service
		Registry svcReg = LocateRegistry.createRegistry(REG_PORT);
		RequestHandler requestHandler = new RequestHandlerImpl(svcPort, this);
		svcReg.rebind(InetAddress.getLocalHost().getHostAddress() + "-request", requestHandler);
		
	}
	
	public void handleRequest (Request request) {
		
		// TODO put the request into a queue, later to SQS
		
		log.info("Received a request and put into queue: " + request);
		queue.enqueue(request);
	}

	public void run() {
		while (true) {
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
