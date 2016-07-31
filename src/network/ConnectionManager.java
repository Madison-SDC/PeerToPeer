package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		
		// We already have a ServerSocket on this port
		if (serverSockets.containsKey(port)) 
			this.addConnection(name, new ConnectionEstablisher(serverSockets.get(port), name, incoming).getConnection());
		
		// Make a new ServerSocket on this Port
		else {
			try { 
				ServerSocket tempSock = new ServerSocket(port);
				serverSockets.put(port, tempSock);
				this.addConnection(name, new ConnectionEstablisher(tempSock, name, incoming).getConnection());
			}
			catch (IOException io) { io.printStackTrace(); }
		}
	}
	
	public synchronized void connectTo(String ip, int port, String name) {
		this.addConnection(name, new ConnectionEstablisher(ip, port, name, incoming).getConnection());
	}
	
	public synchronized void addConnection(String name, Connection conn) { allConnections.put(name, conn); }
	public Connection getConnection(String name) { return allConnections.get(name); }
	
	public SynchronousQueue<ActionItem> getOutgoingQueue() { return outgoing; }
	public SynchronousQueue<ActionItem> getIncomingQueue() { return incoming; }
	
}
