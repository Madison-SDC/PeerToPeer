package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import action.ActionItem;

public class ConnectionManager {

	HashMap<String, Connection> allConnections;
	HashMap<Integer, ServerSocket> serverSockets;
	
	LinkedBlockingQueue<ActionItem> incoming;
	LinkedBlockingQueue<ActionItem> outgoing;
	
	public ConnectionManager() {
		allConnections = new HashMap<String, Connection>();
		serverSockets = new HashMap<Integer, ServerSocket>();
		incoming = new LinkedBlockingQueue<ActionItem>();
		outgoing = new LinkedBlockingQueue<ActionItem>();
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
		ConnectionEstablisher ce = new ConnectionEstablisher(ip, port, name, incoming);
		while(ce.getConnection() == null); // since this blocks, why should I have it threaded?
		this.addConnection(name, ce.getConnection());
	}
	
	public void closeAllConnections() {
		System.out.println("not implemented yet");
	}
	
	public synchronized void addConnection(String name, Connection conn) { allConnections.put(name, conn); }
	public Connection getConnection(String name) { return allConnections.get(name); }
	
	public LinkedBlockingQueue<ActionItem> getOutgoingQueue() { return outgoing; }
	public LinkedBlockingQueue<ActionItem> getIncomingQueue() { return incoming; }
	
}
