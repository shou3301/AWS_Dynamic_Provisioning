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
		
		if (args.length != 1)
			return;
		
		int port = Integer.parseInt(args[0]);
		
		try {
			
			JobScheduler js = new JobScheduler(port);
			new Thread(js, "Thread-JobScheduler").run();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
