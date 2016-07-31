package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

import action.ActionItem;

public class ConnectionManager {

	HashMap<String, Connection> allConnections;
	HashMap<Integer, ServerSocket> serverSockets;
	
	SynchronousQueue<ActionItem> incoming;
	SynchronousQueue<ActionItem> outgoing;
	
	public ConnectionManager() {
		allConnections = new HashMap<String, Connection>();
		serverSockets = new HashMap<Integer, ServerSocket>();
		incoming = new SynchronousQueue<ActionItem>();
		outgoing = new SynchronousQueue<ActionItem>();
	}
	
	public synchronized void listenOn(int port, String name) {
		if (serverSockets.containsKey(port)) 
			new ConnectionEstablisher(serverSockets.get(port), name, this);
		else {
			try { 
				ServerSocket temp = new ServerSocket(port);
				serverSockets.put(port, temp);
				new ConnectionEstablisher(temp, name, this);
			}
			catch (IOException io) { io.printStackTrace(); }
		}
	}
	
	public synchronized void connectTo(String ip, int port, String name) {
		new ConnectionEstablisher(ip, port, name, this);
	}
	
	public synchronized void addConnection(Connection conn) { allConnections.put(conn.getConnectionName(), conn); }
	public synchronized Connection getConnection(String name) { return allConnections.get(name); }
	
	public SynchronousQueue<ActionItem> getOutgoingQueue() { return outgoing; }
	public SynchronousQueue<ActionItem> getIncomingQueue() { return incoming; }
	
}
