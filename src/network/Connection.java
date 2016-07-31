package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

import action.ActionItem;

public class Connection extends Thread implements Runnable {
	
	private boolean alive = true;
	private boolean connected = false;
	private Socket conn;
	private SynchronousQueue<ActionItem> queue;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Object curr;
	private String connectionName;
	
	public Connection(Socket conn, String connectionName, SynchronousQueue<ActionItem> incoming) throws IOException {
		super();
		this.conn = conn;
		this.connectionName = connectionName;
		this.queue = incoming;
		this.start();
	}
	
	public void run() {
		System.out.println("Attempting to start listening thread for '" + connectionName + "'.");
		try {
			in = new ObjectInputStream(conn.getInputStream());
			out = new ObjectOutputStream(conn.getOutputStream());
		} catch (IOException io) { io.printStackTrace(); }
		System.out.println("A listening thread has been started.");
		while (alive) {
			try {
				while ((curr = in.readObject()) != null) queue.put(new ActionItem(curr));
			} catch (Exception io) { 
				alive = false;
				System.out.println(connectionName + " went down.");
			}
		}
	}
	
	public void sendObject(Object e) {
		try { out.writeObject(e);
		} catch (IOException io) { io.printStackTrace(); }
	}
	
	public boolean isConnected() { 
		try { return conn.isConnected(); }
		catch (Exception e) { return false; }
	}
	
	public void kill() {
		alive = false;
		try { 
			conn.close();
			out.close();
			in.close();
		} catch (IOException io) { io.printStackTrace(); }
	}
	
	public String getConnectionName() { return connectionName; }
}