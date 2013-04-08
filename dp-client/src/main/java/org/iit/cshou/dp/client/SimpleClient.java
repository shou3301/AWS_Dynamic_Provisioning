/**
 * 
 */
package org.iit.cshou.dp.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.iit.cshou.dp.intl.Feedback;
import org.iit.cshou.dp.intl.Request;
import org.iit.cshou.dp.intl.RequestHandler;

/**
 * @author cshou
 *
 */
public class SimpleClient {
	
	protected static final int REG_PORT = 6666;

	protected String schedulerAddr = null;
	
	protected int schedulerPort = -1;
	
	protected int localPort = -1;
	
	protected Registry serverReg = null;
	
	protected ConcurrentMap<String, Request> jobList = null;
	
	public SimpleClient (String addr, int schedulerPort, int localPort) throws RemoteException {
		
		this.schedulerAddr = addr;
		this.schedulerPort = schedulerPort;
		this.localPort = localPort;
		
		this.jobList = new ConcurrentHashMap<String, Request>();
		
		serverReg = LocateRegistry.getRegistry(schedulerAddr, schedulerPort);
		
		Registry svcReg = LocateRegistry.createRegistry(REG_PORT);
		FeedbackHandlerImpl feedbackHandler = new FeedbackHandlerImpl(localPort, this);
		svcReg.rebind(this.schedulerAddr + "-feedback", feedbackHandler);
		
	}
	
	public void notify (Feedback feedback) throws AccessException, RemoteException, NotBoundException {
		
		if (feedback.getIsSuccessful()) {	// if job completed successfully
			jobList.remove(feedback.getRequestId());
		}
		else {
			submit(feedback.getRequest());
		}
	}
	
	public void submit (Request request) throws AccessException, RemoteException, NotBoundException {
		// TODO get remote request handler and submit request
		RequestHandler requestHandler = (RequestHandler) serverReg.lookup(schedulerAddr + "-request");
		requestHandler.getRequest(request);
	}
	
	public List<Request> getPendingJobs () {
		return Collections.unmodifiableList((List<Request>) jobList.values());
	}
	
}
