/**
 * 
 */
package org.iit.cshou.dp.scheduler;

/**
 * @author cshou
 *
 */
public class JobSchedulerMain {

	public static void main(String[] args) {
		
		if (args.length != 3)
			return;
		
		int port = Integer.parseInt(args[0]);
		String imgName = args[1];
		String sqsName = args[2];
		
		try {
			
			JobScheduler js = new JobScheduler(port, imgName, sqsName);
			new Thread(js, "Thread-JobScheduler").start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
