package action;

import java.util.concurrent.LinkedBlockingQueue;

public class ActionExecutor extends Thread implements Runnable {
	
	boolean running = true;
	LinkedBlockingQueue<ActionItem> events;
	ActionItem curr;

	public ActionExecutor(LinkedBlockingQueue<ActionItem> events) {
		super();
		this.events = events;
		this.start();
	}
	
	public void run() {
		while (running) {
			try { curr = events.take(); }
			catch (InterruptedException ie) { ie.printStackTrace(); }
			executeEvent(curr);
		}
	}
	
	private void executeEvent(ActionItem event) {
		System.out.println("Event being executed: " + event.toString());
	}
}
