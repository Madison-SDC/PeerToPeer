package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionEstablisher extends Thread implements Runnable {
	
	private boolean isListening = false;
	private ServerSocket listeningSocket;
	private int port;
	private String name;
	private String ip;
	private ConnectionManager cm;
	
	public ConnectionEstablisher(ServerSocket socket, String name, ConnectionManager cm) {
		super();
		listeningSocket = socket;
		this.cm = cm;
		this.name = name;
		this.port = socket.getLocalPort();
		isListening = true;
		this.start();
	}
	
	public ConnectionEstablisher(String ip, int port, String name, ConnectionManager cm) {
		super();
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.cm = cm;
		this.start();
	}
	
	public synchronized void run() {
		if (isListening) {
			System.out.println("Awaiting connection '" + name + "' on port " + port + ".");
			try {
				Socket newConnection = listeningSocket.accept();
				System.out.println(name + " connected!");
				cm.addConnection(name, new Connection(newConnection, name, cm.getIncomingQueue()));
			} catch (IOException io) { System.out.println(io.getMessage()); }
		}
		else {
			try {
				System.out.println("Attempting to establish '" + name + "' to " + ip + " on port " + port + ".");
				Socket newSocket = new Socket(ip, port);
				System.out.println(name + " connected!");
				cm.addConnection(name, new Connection(newSocket, name, cm.getIncomingQueue()));
			} catch (IOException io) { System.out.println(io.getMessage()); }
		}
	}
}
