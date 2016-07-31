package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
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
		this.queue = queue;
		this.start();
	}
	
	public void run() {
		try {
			System.out.println("Reaching out to " + ip + " on port " + port + "");
			connection = new Socket(ip, port);
			in = new ObjectInputStream(connection.getInputStream());
			out = new ObjectOutputStream(connection.getOutputStream());
			System.out.println("Connection to " + ip + " on port " + port + " established.");
		} catch (ConnectException ce)  { 
			System.out.println("Connection to " + ip + " on port " + port + " could not be established.");
		} catch (IOException io) { 
			io.printStackTrace();
		}
		
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
	
	public boolean isConnected() { 
		try { return connection.isConnected(); }
		catch (Exception e) { return false; }
	}
	
	public void kill() {
		running = false;
		try { connection.close(); } catch (IOException io) { io.printStackTrace(); }
	}
}
