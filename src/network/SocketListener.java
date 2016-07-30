package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

import action.ActionItem;

public class SocketListener extends Thread implements Runnable {
	
	private boolean alive = true;
	private boolean connected = false;
	
	private ServerSocket incoming;
	private Socket outgoing;
	private SynchronousQueue<ActionItem> queue;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Object curr;
	
	private String connectionName;
	private int port;
	
	/*
	 * Poll for another connection using the same ServerSocket
	 */
	public SocketListener(ServerSocket incoming, SynchronousQueue<ActionItem> queue, String connectionName) {
		super();
		this.incoming = incoming;
		this.connectionName = connectionName;
		this.queue = queue;
		this.port = incoming.getLocalPort();
		this.start();
	}
	
	public void run() {
		try {
			// instantiate connection
			System.out.println("Waiting for incoming connection on " + port + " . . .");
			outgoing = incoming.accept();
			System.out.println("Connection '" + connectionName + "' established on port " + port + ".");
			out = new ObjectOutputStream(outgoing.getOutputStream());
			in = new ObjectInputStream(outgoing.getInputStream());			
		} catch (IOException io) { io.printStackTrace(); }
		connected = true;
		while (alive) {
			try {
				while ((curr = in.readObject()) != null) processObject();
			} catch (Exception io) { io.printStackTrace(); }
		}
	}
	
	public boolean isConnected() { return connected; }
	public boolean isPolling() { return alive; }
	public void processObject() { queue.add(new ActionItem(curr)); }
	
	public void sendObject(Object e) { 
		try {
			out.writeObject(e);
		} catch (IOException io) { io.printStackTrace(); }
	}
	
	public void kill() {
		connected = false;
		try {
			out.close();
			in.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
}
