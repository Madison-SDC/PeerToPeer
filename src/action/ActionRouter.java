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
		int tries = 5;
		while (true) {
			while ((curr = queue.poll()) != null) {
				tries = 5;
				while (sendActionObject(curr) && tries > 0) {
					mySleep(500); 
					System.out.print("Retry " + tries + " . . . ");
					tries--;
				}
			}
		}
	}

	private static void mySleep(int time) {
		try { Thread.sleep(time); } catch (InterruptedException ie) { ie.printStackTrace(); }
	}

	private synchronized boolean sendActionObject(ActionItem a) {
		if (a.willBroadcast()) { return broadcastActionObject(a); }
		try { 
			currConnection = cm.getConnection(currentSelectedConnection);
			if (currConnection.isAlive()) {
				System.out.println("Sending to: " + currentSelectedConnection + " . . . ");
				currConnection.sendObject(a);
				return false;
			}
			else System.out.println("That connection is dead.");
		} catch (NullPointerException npe) { 
			System.out.println("Connection '" + currentSelectedConnection + "' is not up yet!");
			return true;
		} catch (Exception se) { System.out.println(se.getMessage()); }
		return false;
	}

	private boolean broadcastActionObject(ActionItem a) {
		System.out.println("Not implemented");
		return false;
	}

	public void setCurrentConnection(String name) { currentSelectedConnection = name; }
	public String getCurrentConnectionName() { return currentSelectedConnection; }

}
