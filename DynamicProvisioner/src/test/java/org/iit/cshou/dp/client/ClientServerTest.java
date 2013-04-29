package org.iit.cshou.dp.client;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import org.iit.cshou.dp.intl.Request;
import org.iit.cshou.dp.scheduler.JobScheduler;
import org.iit.cshou.dp.worker.WorkerLeader;
import org.junit.Test;

public class ClientServerTest {

	@Test
	public void testSimpleClient() throws UnknownHostException {

		
//		try {
//			System.out.println(InetAddress.getLocalHost().getHostAddress());
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
		
		String addr = InetAddress.getLocalHost().getHostAddress();
		
		try {
			
			SimpleClient client = new SimpleClient(addr, 6667, 6668);
			
			JobScheduler js = new JobScheduler(6669, "img_name", "test-queue-1");
			new Thread(js, "Thread-JobScheduler").start();
			
			WorkerLeader wl = new WorkerLeader(5000, 10, 5, "test-queue-1");
			wl.start();
			
			String filepath = "request.txt";
			
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String line = null;
			int lineNum = 0;
						
			while ((line = br.readLine()) != null) {
				
				long time = Long.parseLong(line);
				Request req = new SleepRequest(InetAddress.getLocalHost().getHostAddress(), 
						6666, filepath + lineNum, time);
				
				//System.out.println("Getting request from file: \n" + req);
				
				client.submit(req);
				lineNum++;
				
			}
			
			br.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
