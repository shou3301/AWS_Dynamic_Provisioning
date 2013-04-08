/**
 * 
 */
package org.iit.cshou.dp.intl;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author cshou
 *
 */
public interface FeedbackHandler extends Remote {

	public void getFeedback (Feedback feedback) throws RemoteException, NotBoundException;
	
}
