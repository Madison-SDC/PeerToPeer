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
		ConnectionEstablisher ce;
		ServerSocket tempSock = null;
		
		// We already have a ServerSocket on this port
		if (serverSockets.containsKey(port)) tempSock = serverSockets.get(port);
		
		// Make a new ServerSocket on this Port
		else {
			try { 
				tempSock = new ServerSocket(port);
				serverSockets.put(port, tempSock);
			} catch (IOException io) { io.printStackTrace(); }
		}
		ce = new ConnectionEstablisher(tempSock, name, incoming);
		while(ce.getConnection() == null); // since this blocks, why should I have it threaded?
		this.addConnection(name, ce.getConnection());
	}
	
	public synchronized void connectTo(String ip, int port, String name) {
		this.addConnection(name, new ConnectionEstablisher(ip, port, name, incoming).getConnection());
	}
	
	public synchronized void addConnection(String name, Connection conn) { allConnections.put(name, conn); }
	public Connection getConnection(String name) { return allConnections.get(name); }
	
	public SynchronousQueue<ActionItem> getOutgoingQueue() { return outgoing; }
	public SynchronousQueue<ActionItem> getIncomingQueue() { return incoming; }
	
}
