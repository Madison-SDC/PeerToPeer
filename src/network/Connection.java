package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import action.ActionItem;

public class Connection extends Thread implements Runnable {

	private boolean alive = true;
	private boolean fromListening = false;
	private LinkedBlockingQueue<ActionItem> queue;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Object curr;
	private Socket sock;
	private ServerSocket serverSock;
	private String connectionName;

	/*
	public Connection(ObjectOutputStream out, ObjectInputStream in, String name, LinkedBlockingQueue<ActionItem> incoming) throws IOException {
		super();
		this.connectionName = name;
		this.queue = incoming;
		this.out = out;
		this.in = in;
		this.start();
	}
	*/
	
	public Connection(Socket socket, String name, LinkedBlockingQueue<ActionItem> incoming) {
		super();
		this.connectionName = name;
		this.queue = incoming;
		this.sock = socket;
	}
	
	public Connection(ServerSocket socket, String name, LinkedBlockingQueue<ActionItem> incoming) {
		super();
		fromListening = true;
		this.serverSock = socket;
		this.connectionName = name;
		this.queue = incoming;
		this.start();
	}

	public void run() {
		if (fromListening) {
			try { sock = serverSock.accept(); } 
			catch (IOException io) { System.out.println("Could not establish connection '" + connectionName + "': " + io.getMessage()); }
		}
		try {
			this.out = new ObjectOutputStream(sock.getOutputStream());
			this.in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException io) { System.out.println("Could not establish connection '" + connectionName + "': " + io.getMessage()); }
		System.out.println(connectionName + " connected!");
		while (alive) {
			try {
				while ((curr = in.readObject()) != null) queue.put(new ActionItem(curr));
			} catch (Exception io) { 
				alive = false;
				System.out.println(connectionName + " went down.");
				this.kill();
			}
		}
	}

	public void sendObject(Object e) {
		try { out.writeObject(e);
		} catch (IOException io) { io.printStackTrace(); }
	}

	public void kill() {
		alive = false;
		try { 
			out.close();
			in.close();
		} catch (IOException io) { io.printStackTrace(); }
	}

	public String getConnectionName() { return connectionName; }
	public boolean isConnected() { return alive; }
}
