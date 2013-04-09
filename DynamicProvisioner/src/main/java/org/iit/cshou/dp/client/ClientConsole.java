/**
 * 
 */
package org.iit.cshou.dp.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import org.iit.cshou.dp.intl.Request;

/**
 * @author cshou
 *
 */
public class ClientConsole {
	
	private final static String EXIT = "exit";
	
	private final static String SUBMIT = "submit";
	
	protected static final int REG_PORT = 6666;

	private static String schedulerAddr = null;
	
	private static int schedulerPort = -1;
	
	private static int localPort = -1;
	
	public static void main(String[] args) throws IOException {
		
		if (args.length != 3) {
			printHelp();
			return;
		}
		
		schedulerAddr = args[0];
		schedulerPort = Integer.parseInt(args[1]);
		localPort = Integer.parseInt(args[2]);
		
		// TODO RMI object, and pass in these two parameters
		SimpleClient client = new SimpleClient(schedulerAddr, schedulerPort, localPort);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String line = null;
		
		while (true) {
			line = br.readLine();
			
			if (line.equalsIgnoreCase(EXIT)) {
				break;
			}
			else if (line == null || line.equals("")) {
				continue;
			}
			else {
				
				String[] params = line.split(" ");
				
				if (params[0].equalsIgnoreCase(SUBMIT) &&
						params.length == 2) {
					
					String filepath = params[1];
					
					// TODO parse file and send
					try {
						submitByFile(filepath, client);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
				}
				else {
					break;
				}
			}
		}
		
		br.close();
		
	}
	
	private static void printHelp () {
		System.out.println("java -jar client.jar [IP of scheduler] [port of scheduler] [localp port]");
		System.out.println("submit [path to the job file]");
	}

	// this part should be moved to another class if job types become more
	private static void submitByFile (String path, SimpleClient client) throws Exception {
		
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = null;
		int lineNum = 0;
		
		// file should only contain a number for each line
		
		while ((line = br.readLine()) != null) {
			long time = Long.parseLong(line);
			Request req = new SleepRequest(InetAddress.getLocalHost().getHostAddress(), 
					REG_PORT, path + lineNum, time);
			client.submit(req);
			lineNum++;
		}
		
		br.close();
		
	}
	
}
