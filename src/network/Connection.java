package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

import action.ActionItem;

public class Connection extends Thread implements Runnable {

	private boolean alive = true;
	private SynchronousQueue<ActionItem> queue;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Object curr;
	private String connectionName;

	public Connection(ObjectOutputStream out, ObjectInputStream in, String name, SynchronousQueue<ActionItem> incoming) throws IOException {
		super();
		this.connectionName = name;
		this.queue = incoming;
		this.out = out;
		this.in = in;
		this.start();
	}

	public void run() {
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
