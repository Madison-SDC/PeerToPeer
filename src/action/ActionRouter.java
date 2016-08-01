package action;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import network.Connection;
import network.ConnectionManager;

public class ActionRouter extends Thread implements Runnable {

	private String currentSelectedConnection;
	private List<String> currentRecipients;
	private LinkedBlockingQueue<ActionItem> queue;
	private ConnectionManager cm;
	private ActionItem curr;
	private Connection currConnection;

	public ActionRouter(LinkedBlockingQueue<ActionItem> queue, ConnectionManager cm) {
		super();
		this.queue = queue;
		this.cm = cm;
		this.start();
	}

	public void run() {
		while (true) while ((curr = queue.poll()) != null) sendActionObject(curr);
	}

	private synchronized void sendActionObject(ActionItem a) {
		if (a.willBroadcast()) { broadcastActionObject(a); return; }
		currConnection = cm.getConnection(currentSelectedConnection);
		if (currConnection == null) System.out.println("'" + currentSelectedConnection + "' does not exist!");
		else {
			if (currConnection.isConnected()) currConnection.sendObject(a);
			else {
				if (currConnection.isDead()) {
					System.out.println("'" + currentSelectedConnection + "' was dead. I am removing it.");
					cm.closeConnection(currentSelectedConnection);
				}
				else System.out.println("'" + currentSelectedConnection + "' not connected yet!");
			}
		}
	}

	private boolean broadcastActionObject(ActionItem a) {
		System.out.println("Not implemented");
		return false;
	}

	public void setCurrentConnection(String name) { currentSelectedConnection = name; }
	public String getCurrentConnectionName() { return currentSelectedConnection; }
}
