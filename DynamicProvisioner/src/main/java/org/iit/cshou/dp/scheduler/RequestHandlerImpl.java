/**
 * 
 */
package org.iit.cshou.dp.scheduler;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.iit.cshou.dp.intl.Request;
import org.iit.cshou.dp.intl.RequestHandler;

/**
 * @author cshou
 *
 */
public class RequestHandlerImpl extends UnicastRemoteObject implements RequestHandler {

	private static final long serialVersionUID = -7293426315949967934L;
	
	protected JobScheduler scheduler = null;

	protected RequestHandlerImpl(int port, JobScheduler scheduler) throws RemoteException {
		super(port);
		this.scheduler = scheduler;
	}

	public void getRequest(Request request) throws RemoteException, NotBoundException {
		// TODO handle request by passing it to the scheduler
		scheduler.handleRequest(request);
	}

}
