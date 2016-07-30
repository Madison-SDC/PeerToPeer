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
		if (currentConnectionType == 0) currentListeningConnection.sendObject(a);
		else currentSendingConnection.sendObject(a);
	}
	
	public synchronized void setCurrentConnection(String connection) { 
		currentSelectedConnection = connection;
		currentConnectionType = cm.getConnectionType(connection);
		
		// Was established via ServerSocket
		if (currentConnectionType == 0) currentListeningConnection = cm.getListeningConnection(connection);
		
		// Was established via Socket
		else currentSendingConnection = cm.getSendingConnection(connection);
			
		
	}
	public String getCurrentConnectionString() { return currentSelectedConnection; }
	
}
