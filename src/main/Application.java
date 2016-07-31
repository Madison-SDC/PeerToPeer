package main;

import action.ActionExecutor;
import action.ActionRouter;
import action.InputHandler;
import network.ConnectionManager;

public class Application {
	
	private static ConnectionManager cm;
	private static InputHandler ih;
	private static ActionRouter ar;
	private static ActionExecutor ae;
	
	public static void main(String[] args) {
		
		int currComputer = 1;
		
		cm = new ConnectionManager();
		ih = new InputHandler(cm.getOutgoingQueue());
		ar = new ActionRouter(cm.getOutgoingQueue(), cm);
		ae = new ActionExecutor(cm.getIncomingQueue());
		
		switch (currComputer) {
		case 1: testComputer1(); break;
		case 2: testComputer2(); break;
		case 3: testComputer3(); break;
		}
		
		while (true) {
			try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
			System.out.println("Main woke up.");
		}
	}
	
	private static void testComputer1() {
		
		// await first connection
		cm.listenOn(61111, "testLocalIncoming1");
		ar.setCurrentConnection("testLocalIncoming1");
		while (!cm.getListeningConnection("testLocalIncoming1").isConnected());
		
		// await second connection
		cm.listenOn(61111, "testLocalIncoming2");
		while (!cm.getListeningConnection("testLocalIncoming2").isConnected());
	}
	
	private static void testComputer2() {
		cm.connectTo("10.0.0.12", 61111, "testOutgoing");
	}
	
	private static void testComputer3() {
		cm.connectTo("10.0.0.12", 61111, "testOutgoing");
	}
}
