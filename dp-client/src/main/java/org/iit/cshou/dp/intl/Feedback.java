/**
 * 
 */
package org.iit.cshou.dp.intl;

import java.io.Serializable;

/**
 * @author cshou
 *
 */
public abstract class Feedback implements Serializable {

	private static final long serialVersionUID = -8889168310944419608L;
	
	protected String runningInfo = null;
	
	protected Boolean isSuccessful = false;
	
	private Request request = null;

	public String getRunningInfo() {
		return runningInfo;
	}

	public void setRunningInfo(String runningInfo) {
		this.runningInfo = runningInfo;
	}

	public Boolean getIsSuccessful() {
		return isSuccessful;
	}

	public void setIsSuccessful(Boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
	
	public String getRequestId () {
		return this.request.getRequestId();
	}

}
