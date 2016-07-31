package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import action.ActionItem;

public class ConnectionEstablisher extends Thread implements Runnable {

	private ServerSocket listeningSocket;
	private int port;
	private String name;
	private String ip;
	private Connection conn;
	private Socket initialSocket;
	private boolean listening = false;
	private boolean obtained = false;
	private LinkedBlockingQueue<ActionItem> incoming;

	/**
	 * For connections initialized with a ServerSocket.
	 * @param socket
	 * @param name
	 * @param incoming
	 */
	public ConnectionEstablisher(ServerSocket socket, String name, LinkedBlockingQueue<ActionItem> incoming) {
		super();
		listeningSocket = socket;
		this.name = name;
		this.port = socket.getLocalPort();
		this.incoming = incoming;
		listening = true;
		System.out.println("Awaiting connection '" + name + "' on port " + port + ".");
		this.start();	
	}

	/**
	 * For connections initialized by requests.
	 * @param ip
	 * @param port
	 * @param name
	 * @param incoming
	 */
	public ConnectionEstablisher(String ip, int port, String name, LinkedBlockingQueue<ActionItem> incoming) {
		super();
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.incoming = incoming;
		System.out.println("Attempting to establish '" + name + "' to " + ip + " on port " + port + ".");
		this.start();
	}

	public void run() {
		if (listening) {
			try {
				Socket newConnection = listeningSocket.accept(); // blocking
				ObjectOutputStream out = new ObjectOutputStream(newConnection.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(newConnection.getInputStream());
				conn = new Connection(out, in, name, incoming);
				System.out.println(name + " connected!");
			} catch (IOException io) { System.out.println(io.getMessage()); }
		}
		else {
			try {
				initialSocket = new Socket(ip, port); // blocking?
				ObjectOutputStream out = new ObjectOutputStream(initialSocket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(initialSocket.getInputStream());
				conn = new Connection(out, in, name, incoming);
				System.out.println(name + " connected!");
			} catch (IOException io) { System.out.println(io.getMessage()); }
		}
		while (!obtained);
	}

	public Connection getConnection() { 
		obtained = true;
		return conn;
	}
	
	public Socket getSocket() { return initialSocket; }
}
