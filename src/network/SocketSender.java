package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

import action.ActionItem;

public class SocketSender extends Thread implements Runnable{

	private boolean running = true;
	private Socket connection;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private SynchronousQueue<ActionItem> queue;
	private String ip;
	private int port;
	private Object curr;
	private String connectionName;
	
	public SocketSender(String ip, int port, SynchronousQueue<ActionItem> queue, String connectionName) {
		super();
		this.ip = ip;
		this.port = port;
		this.connectionName = connectionName;
		try { 
			connection = new Socket(ip, port);
			in = new ObjectInputStream(connection.getInputStream());
			out = new ObjectOutputStream(connection.getOutputStream());
		} catch (IOException io) { io.printStackTrace(); }
		this.queue = queue;
		this.start();
	}
	
	public void run() {
		if(!waitForConnection()) {
			running = false;
			System.out.println("Connection to " + ip + " on port " + port + " was not established.");
			this.kill();
		}
		System.out.println("Connection to " + ip + " on port " + port + " established.");
		while (running) {
			try {
				while ((curr = in.readObject()) != null) queue.put(new ActionItem(curr));
			} catch (Exception io) {
				running = false;
				System.out.println(connectionName + " went down.");
				//io.printStackTrace();
			}
		}
	}
	
	public void sendObject(Object e) {
		try {
			out.writeObject(e);
		} catch (IOException io) { io.printStackTrace(); }
	}
	
	private boolean waitForConnection() {
		int numTries = 0;
		while (!connection.isConnected() && numTries < 10) {
			try { this.sleep(1000); } catch (InterruptedException ie) { ie.printStackTrace(); }
			numTries++;
		}
		if (numTries == 10) return false;
		return true;
	}
	
	public boolean isConnected() { return connection.isConnected(); }
	
	public void kill() {
		running = false;
		try { connection.close(); } catch (IOException io) { io.printStackTrace(); }
	}
}
