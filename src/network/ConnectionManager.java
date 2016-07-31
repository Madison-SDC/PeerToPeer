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
	
	//List<ConnectionEstablisher> ce;
	ConnectionEstablisher temp;
	
	public ConnectionManager() {
		allConnections = new HashMap<String, Connection>();
		serverSockets = new HashMap<Integer, ServerSocket>();
		incoming = new SynchronousQueue<ActionItem>();
		outgoing = new SynchronousQueue<ActionItem>();
		//ce = new ArrayList<ConnectionEstablisher>();
	}
	
	public synchronized void listenOn(int port, String name) {
		if (serverSockets.containsKey(port)) {
			temp = new ConnectionEstablisher(serverSockets.get(port), name, incoming);
			//ce.add(temp);
			this.addConnection(name, temp.getConnection());
		}
		else {
			try { 
				ServerSocket tempSock = new ServerSocket(port);
				serverSockets.put(port, tempSock);
				temp = new ConnectionEstablisher(tempSock, name, incoming);
				//ce.add(temp);
				this.addConnection(name, temp.getConnection());
			}
			catch (IOException io) { io.printStackTrace(); }
		}
	}
	
	public synchronized void connectTo(String ip, int port, String name) {
		temp = new ConnectionEstablisher(ip, port, name, incoming);
		//ce.add(temp);
		this.addConnection(name, temp.getConnection());
	}
	
	public synchronized void addConnection(String name, Connection conn) { allConnections.put(name, conn); }
	public Connection getConnection(String name) { return allConnections.get(name); }
	
	public SynchronousQueue<ActionItem> getOutgoingQueue() { return outgoing; }
	public SynchronousQueue<ActionItem> getIncomingQueue() { return incoming; }
	
}
