package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import action.ActionItem;
import exceptions.AttemptedConnectionError;

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
	
	public void listenOn(int port, String name) throws AttemptedConnectionError {
		if (allConnections.containsKey(name))
			throw new AttemptedConnectionError("You already have a connection with that name!");
		
		ServerSocket tempSock = null;
		
		// We already have a ServerSocket on this port
		if (serverSockets.containsKey(port)) {
			if (this.currentConnectionPending(port)) throw new AttemptedConnectionError("There is a pending connection on that port!");
			tempSock = serverSockets.get(port);
		}
		
		// Make a new ServerSocket on this Port
		else {
			try { 
				tempSock = new ServerSocket(port);
				serverSockets.put(port, tempSock);
			} catch (IOException io) { throw new AttemptedConnectionError(io.getMessage()); }
		}
		System.out.println("Awaiting connection '" + name + "' on port " + port + ".");
		allConnections.put(name, new Connection(tempSock, name, incoming));
	}
	
	public void connectTo(String ip, int port, String name) throws AttemptedConnectionError {
		if (allConnections.containsKey(name))
			throw new AttemptedConnectionError("You already have a connection with that name!");
		
		System.out.println("Attempting to establish '" + name + "' to " + ip + " on port " + port + ".");
		try {
			allConnections.put(name, new Connection(new Socket(InetAddress.getByName(ip), port), name, incoming));
		} catch (Exception e) { throw new AttemptedConnectionError(e.getMessage()); }
	}
	
	public void closeAllConnections() {
		System.out.println("not implemented yet");
	}
	
	private boolean currentConnectionPending(int port) {
		Iterator<Map.Entry<String, Connection>> itr = allConnections.entrySet().iterator();
		while (itr.hasNext()) 
			if (itr.next().getValue().isPending()) return true;
		return false;
	}
	
	public Connection getConnection(String name) { return allConnections.get(name); }
	public void closeConnection(String name) { allConnections.remove(name); }
	public LinkedBlockingQueue<ActionItem> getOutgoingQueue() { return outgoing; }
	public LinkedBlockingQueue<ActionItem> getIncomingQueue() { return incoming; }
	
}
