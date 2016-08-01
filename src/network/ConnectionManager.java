package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import action.ActionItem;

public class ConnectionManager {

	HashMap<String, Connection> allConnections;
	HashMap<Integer, ServerSocket> serverSockets;
	HashMap<String, ConnectionEstablisher> connectionChecker;
	
	LinkedBlockingQueue<ActionItem> incoming;
	LinkedBlockingQueue<ActionItem> outgoing;
	
	public ConnectionManager() {
		allConnections = new HashMap<String, Connection>();
		serverSockets = new HashMap<Integer, ServerSocket>();
		connectionChecker = new HashMap<String, ConnectionEstablisher>();
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
		connectionChecker.put(name, ce);
	}
	
	public synchronized void connectTo(String ip, int port, String name) {
		ConnectionEstablisher ce = new ConnectionEstablisher(ip, port, name, incoming);
		connectionChecker.put(name, ce);
	}
	
	public void closeAllConnections() {
		System.out.println("not implemented yet");
	}
	
	public synchronized void addConnection(String name, Connection conn) { allConnections.put(name, conn); }
	public Connection getConnection(String name) {
		
		// Connection has been retrieved & established
		if (allConnections.containsKey(name)) return allConnections.get(name);
		
		// Connection hasn't been retrieved
		Connection temp = connectionChecker.get(name).getConnection();
		
		// If it wasn't ready, return null
		if (temp == null) return null;
		
		// If it was ready, garbage collect connectionChecker, add it to connections
		allConnections.put(name, temp);
		connectionChecker.remove(name);
		return temp;
	}
	
	public LinkedBlockingQueue<ActionItem> getOutgoingQueue() { return outgoing; }
	public LinkedBlockingQueue<ActionItem> getIncomingQueue() { return incoming; }
	
}
