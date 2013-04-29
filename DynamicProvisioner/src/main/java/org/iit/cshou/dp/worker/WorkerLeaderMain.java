/**
 * 
 */
package org.iit.cshou.dp.worker;

/**
 * @author cshou
 *
 */
public class WorkerLeaderMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 4) {
			return;
		}
		
		long interval = Long.parseLong(args[0]);
		int threshold = Integer.parseInt(args[1]);
		int batchNum = Integer.parseInt(args[2]);
		String sqsName = args[3];
		
		// for test, use median interval, small threshold and batchNum
		WorkerLeader wl = new WorkerLeader(interval, threshold, batchNum, sqsName);
		wl.start();
		
	}

}
