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
		
		cm = new ConnectionManager();
		ih = new InputHandler(cm.getOutgoingQueue());
		ar = new ActionRouter(cm.getOutgoingQueue(), cm);
		ae = new ActionExecutor(cm.getIncomingQueue());
		
		cm.listenOn(61111, "testLocalIncoming");
		
		cm.connectTo("127.0.0.1", 61111, "testLocalOutgoing");
		ar.setCurrentConnection("testLocalOutgoing");
		
		while (true) {
			try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
			System.out.println("Main woke up.");
		}
	}
}
