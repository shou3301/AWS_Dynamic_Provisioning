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
public interface RequestHandler extends Remote {

	public void getRequest (Request request) throws RemoteException, NotBoundException;
	
}
