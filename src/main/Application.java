package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;

import action.ActionExecutor;
import action.ActionItem;
import action.ActionRouter;
import network.ConnectionManager;

public class Application {
	
	private static ConnectionManager cm = new ConnectionManager();
	private static ActionRouter ar;
	private static ActionExecutor ae;
	private static LinkedBlockingQueue<ActionItem> userInput;
	private static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	private static boolean running = true;
	
	public static void main(String[] args) {
		int currComputer = 1;
		ar = new ActionRouter(cm.getOutgoingQueue(), cm);
		ae = new ActionExecutor(cm.getIncomingQueue());
		userInput = cm.getOutgoingQueue();
		
		switch (currComputer) {
		case 1: testComputer1(); break;
		case 2: testComputer2(); break;
		case 3: testComputer3(); break;
		}

		String userStringInput;
		while (running) {
			try {
				while ((userStringInput = stdIn.readLine()) != null) processInput(userStringInput);
			} catch (IOException io) { io.printStackTrace(); }
		}
	}
	
	private static void processInput(String in) {
		if (in.equals("quit")) { running = false; return; }
		System.out.println("Processing: " + in);
		try { userInput.put(new ActionItem(in)); } 
		catch (InterruptedException ie) { ie.printStackTrace(); }
	}
	
	private static void testComputer1() {
		
		// await first connection
		cm.listenOn(61111, "testLocalIncoming1");
		ar.setCurrentConnection("testLocalIncoming1");
		
		// await second connection
		//while(cm.getConnection("testLocalIncoming1") == null);
		//cm.listenOn(61111, "testLocalIncoming2");
	}
	
	private static void testComputer2() {
		cm.connectTo("10.0.0.12", 61111, "testOutgoing");
		ar.setCurrentConnection("testOutgoing");
	}
	
	private static void testComputer3() {
		cm.connectTo("10.0.0.12", 61111, "testOutgoing");
		ar.setCurrentConnection("testOutgoing");
	}
}
