/**
 * 
 */
package org.iit.cshou.dp.intl;

import java.io.Serializable;

/**
 * @author cshou
 *
 */
public abstract class Request implements Serializable {

	protected static final long serialVersionUID = 1L;
	
	protected String senderAddr = null;
	
	protected int senderPort = -1;
	
	protected String requestId = null;

	public String getSenderAddr() {
		return senderAddr;
	}

	public void setSenderAddr(String senderAddr) {
		this.senderAddr = senderAddr;
	}

	public int getSenderPort() {
		return senderPort;
	}

	public void setSenderPort(int senderPort) {
		this.senderPort = senderPort;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	/**
	 * @throws Exception
	 * real implementation of what to execute of the request
	 */
	public abstract void execute () throws Exception;
	
	@Override
	public String toString () {
		return "[Sender Addr: " + this.senderAddr + 
				"] [Sender Port : " + this.senderPort +
				"] [Request ID: " + this.requestId + "]";
	}

}
