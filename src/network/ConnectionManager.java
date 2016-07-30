package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

import action.ActionItem;

public class ConnectionManager {
	
	// One instance of a ServerSocket per port listening
	HashMap<String, ServerSocket> incoming;
	
	// Keeps track of all active connections, multiple can be on one port
	HashMap<String, SocketListener> activeIncomingConnections;
	
	// All active connections that were established outwardly
	HashMap<String, SocketSender> activeOutgoingConnections;
	
	// Keeps track of which connection is on which port
	HashMap<String, Integer> portmap;
	
	// Keeps track of whether or not connections are incoming or outgoing
	// 0: Incoming 1: Outgoing
	HashMap<String, Integer> connectionType;
	
	// Keeps track of how many connections exist for a port
	HashMap<Integer, Integer> numConnections;
	
	// The data structure that will allow events to be handled thread-safely
	SynchronousQueue<ActionItem> incomingQueue;
	
	// Handling outgoing events thread safely
	SynchronousQueue<ActionItem> outgoingQueue;
	
	public ConnectionManager() {
		incomingQueue = new SynchronousQueue<ActionItem>();
		outgoingQueue = new SynchronousQueue<ActionItem>();
		portmap = new HashMap<String, Integer>();
		incoming = new HashMap<String, ServerSocket>();
		activeIncomingConnections = new HashMap<String, SocketListener>();
		activeOutgoingConnections = new HashMap<String, SocketSender>();
		numConnections = new HashMap<Integer, Integer>();
	}
	
	public void listenOn(int port, String connectionName) {
		if (incoming.containsKey(connectionName)) {
			System.out.println("Connection named " + connectionName + " already exists!");
			return;
		}
		
		// poll for a new connection on an existing ServerSocket/port
		if (incoming.get(port) != null) {
			activeIncomingConnections.put(connectionName, new SocketListener(incoming.get(port), incomingQueue, connectionName));
			numConnections.put(port, numConnections.get(port) + 1);
		}
		
		// poll for a connection on a new port/ServerSocket
		try {
			ServerSocket newPort = new ServerSocket(port);
			incoming.put(connectionName, newPort);
			portmap.put(connectionName, port);
			activeIncomingConnections.put(connectionName, new SocketListener(newPort, incomingQueue, connectionName));
			connectionType.put(connectionName, 0);
			numConnections.put(port, 1);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void connectTo(String ip, int port, String connectionName) {
		activeOutgoingConnections.put(connectionName, new SocketSender(ip, port, incomingQueue));
		connectionType.put(connectionName, 1);
	}
	
	public boolean checkConnected(String connectionName) { 
		if (activeIncomingConnections.get(connectionName).isConnected()) return true;
		return activeOutgoingConnections.get(connectionName).isConnected();
	}
	
	public int checkNumConnections(int port) { 
		return numConnections.get(port);
	}
	
	public void killConnection(String connectionName) {
		if (this.isOutgoing(connectionName)) activeOutgoingConnections.get(connectionName).kill();
		else activeIncomingConnections.get(connectionName).kill();
	}
	
	public boolean isOutgoing(String connectionName) {
		if (connectionType.get(connectionName) == 1) return true; 
		return false;
	}
	
	public boolean isIncoming(String connectionName) {
		if (connectionType.get(connectionName) == 0) return true; 
		return false;
	}
	
}
