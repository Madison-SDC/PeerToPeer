package action;

import java.util.List;
import java.util.concurrent.SynchronousQueue;

import network.Connection;
import network.ConnectionManager;

public class ActionRouter extends Thread implements Runnable {

	private String currentSelectedConnection;
	private List<String> currentRecipients;
	private SynchronousQueue<ActionItem> queue;
	private ConnectionManager cm;
	private ActionItem curr;
	private Connection currConnection;

	
	public ActionRouter(SynchronousQueue<ActionItem> queue, ConnectionManager cm) {
		super();
		this.queue = queue;
		this.cm = cm;
		this.start();
	}
	
	public void run() {
		while (true) {
			while ((curr = queue.poll()) != null) sendActionObject(curr);
		}
	}
	
	private synchronized void sendActionObject(ActionItem a) {
		if (a.willBroadcast()) { broadcastActionObject(a); return; }
		if (currConnection.isConnected()) {
			System.out.print("Sending to: " + currConnection.getConnectionName() + " . . . ");
			currConnection.sendObject(a);
		}
		else System.out.println("Current connection is down!");
	}
	
	private synchronized void broadcastActionObject(ActionItem a) {
		System.out.println("Not implemented");
	}
	
	public synchronized void setCurrentConnection(String name) { 
		currConnection = cm.getConnection(name);
	}
	
	public String getCurrentConnectionName() { return currentSelectedConnection; }
	
}
