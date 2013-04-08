/**
 * 
 */
package org.iit.cshou.dp.client;

import org.iit.cshou.dp.intl.Feedback;
import org.iit.cshou.dp.intl.Request;

/**
 * @author cshou
 *
 */
public class SimpleFeedback extends Feedback {

	public SimpleFeedback (String info, Boolean isSuccessful, Request request) {
		this.runningInfo = info;
		this.isSuccessful = isSuccessful;
		this.setRequest(request);
	}
	
}
