package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

import action.ActionItem;

public class ConnectionEstablisher {
	
	private ServerSocket listeningSocket;
	private int port;
	private String name;
	private String ip;
	private Connection conn;
	
	public ConnectionEstablisher(ServerSocket socket, String name, SynchronousQueue<ActionItem> incoming) {
		listeningSocket = socket;
		this.name = name;
		this.port = socket.getLocalPort();
		
		System.out.println("Awaiting connection '" + name + "' on port " + port + ".");
		try {
			Socket newConnection = listeningSocket.accept();
			conn = new Connection(newConnection, name, incoming);
			System.out.println(name + " connected!");
		} catch (IOException io) { System.out.println(io.getMessage()); }
		
	}
	
	public ConnectionEstablisher(String ip, int port, String name, SynchronousQueue<ActionItem> incoming) {
		this.ip = ip;
		this.port = port;
		this.name = name;
		System.out.println("Attempting to establish '" + name + "' to " + ip + " on port " + port + ".");
		try {
			Socket newSocket = new Socket(ip, port);
			conn = new Connection(newSocket, name, incoming);
			System.out.println(name + " connected!");
		} catch (IOException io) { System.out.println(io.getMessage()); }
	}
	
	public Connection getConnection() { return conn; }
}
