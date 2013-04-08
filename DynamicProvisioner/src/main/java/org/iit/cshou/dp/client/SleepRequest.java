/**
 * 
 */
package org.iit.cshou.dp.client;

import org.iit.cshou.dp.intl.Request;

/**
 * @author cshou
 *
 */
public class SleepRequest extends Request {

	protected long sleepInterval = 0;
	
	public SleepRequest (String addr, int port, String id, long sleepInterval) {
		this.senderAddr = addr;
		this.senderPort = port;
		this.requestId = id;
		this.sleepInterval = sleepInterval;
	}
	
	public long getSleepInterval () {
		return this.sleepInterval;
	}

	@Override
	public void execute() throws Exception {
		Thread.sleep(this.sleepInterval);
	}
	
}
