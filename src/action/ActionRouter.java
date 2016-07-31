package action;

import java.util.concurrent.SynchronousQueue;

import network.ConnectionManager;
import network.SocketListener;
import network.SocketSender;

public class ActionRouter extends Thread implements Runnable{

	private String currentSelectedConnection;
	private SynchronousQueue<ActionItem> queue;
	private ConnectionManager cm;
	private ActionItem curr;
	private int currentConnectionType;
	private SocketListener currentListeningConnection;
	private SocketSender currentSendingConnection;
	
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
	
	private void sendActionObject(ActionItem a) {
		System.out.print("Sending to: " + currentSelectedConnection + " . . . ");
		if (currentConnectionType == 0) {
			if (currentListeningConnection.isConnected()) currentListeningConnection.sendObject(a);
			else System.out.println("Your current selected connection is dead, or not established yet!");
		}
		if (currentConnectionType == 1) {
			if (currentSendingConnection.isConnected()) currentSendingConnection.sendObject(a);
			else System.out.println("Your current selected connection is dead, or not established yet!");
		}
	}
	
	private void broadcastActionObject(ActionItem a) {
		
	}
	
	public synchronized void setCurrentConnection(String connection, int connectionType) { 
		currentSelectedConnection = connection;
		currentConnectionType = connectionType;
		
		// Was established via ServerSocket
		if (currentConnectionType == 0) currentListeningConnection = cm.getListeningConnection(connection);
		
		// Was established via Socket
		else currentSendingConnection = cm.getSendingConnection(connection);
	}
	public String getCurrentConnectionString() { return currentSelectedConnection; }
	
}
