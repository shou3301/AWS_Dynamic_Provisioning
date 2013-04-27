/**
 * 
 */
package org.iit.cshou.dp.worker;

import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.log4j.Logger;
import org.iit.cshou.dp.client.SimpleFeedback;
import org.iit.cshou.dp.intl.Feedback;
import org.iit.cshou.dp.intl.FeedbackHandler;
import org.iit.cshou.dp.intl.Request;

/**
 * @author cshou
 *
 */
public class Worker implements Runnable {
	
	private static Logger log = Logger.getLogger(Worker.class);
	
	private Request request = null;
	
	private WorkerLeader leader = null;
	
	public Worker (Request request, WorkerLeader leader) {
		this.request = request;
		this.leader = leader;
	}

	public void run() {
		
		FeedbackHandler feedbackHandler = 
				(FeedbackHandler) getFeedbackHander(request.getSenderAddr(), request.getSenderPort());
		
		leader.notifyStart();
		
		try {
			
			log.info("Running job: " + request);
			
			request.execute();
			
			String info = "Was ran on " + InetAddress.getLocalHost().getHostAddress();
			
			Feedback feedback = new SimpleFeedback(info, true, request);
			
			try {
				log.info("Sending feedback to client...");
				feedbackHandler.getFeedback(feedback);
			} catch (Exception e) {
				log.info(e);
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			String info = "Job failed";
			
			Feedback feedback = new SimpleFeedback(info, false, request);
			
			try {
				feedbackHandler.getFeedback(feedback);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		finally {
			// notify the leader that the job has been finished
			leader.notifyFinish();
		}	
		
	}
	
	private Remote getFeedbackHander (String client, int port) {
		
		Remote result = null;
		
		try {
			
			Registry svcReg = LocateRegistry.getRegistry(client, port);
			result = svcReg.lookup(client + "-feedback");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
