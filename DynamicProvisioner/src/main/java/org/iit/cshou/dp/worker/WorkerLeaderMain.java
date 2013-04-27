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
		
		if (args.length != 3) {
			return;
		}
		
		long interval = Long.parseLong(args[0]);
		int threshold = Integer.parseInt(args[1]);
		int batchNum = Integer.parseInt(args[2]);
		
		// for test, use median interval, small threshold and batchNum
		WorkerLeader wl = new WorkerLeader(interval, threshold, batchNum);
		wl.start();
		
	}

}
