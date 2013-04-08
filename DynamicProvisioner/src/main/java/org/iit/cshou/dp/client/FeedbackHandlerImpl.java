/**
 * 
 */
package org.iit.cshou.dp.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.iit.cshou.dp.intl.Feedback;
import org.iit.cshou.dp.intl.FeedbackHandler;

/**
 * @author cshou
 *
 */
public class FeedbackHandlerImpl extends UnicastRemoteObject implements FeedbackHandler {
	
	private static final long serialVersionUID = -4573446662595009314L;
	
	protected SimpleClient client = null;
	
	public FeedbackHandlerImpl (int port, SimpleClient client) throws RemoteException {
		super(port);
		this.client = client;
	}

	public void getFeedback(Feedback feedback) throws RemoteException, NotBoundException {
		this.client.notify(feedback);
	}

}
