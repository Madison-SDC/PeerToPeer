package action;

import java.util.concurrent.SynchronousQueue;

public class ActionExecutor extends Thread implements Runnable {
	
	boolean running = true;
	SynchronousQueue<ActionItem> events;
	ActionItem curr;

	public ActionExecutor(SynchronousQueue<ActionItem> events) {
		super();
		this.events = events;
		this.start();
	}
	
	public void run() {
		while (running) 
			while ((curr = events.poll()) != null) executeEvent(curr);
	}
	
	private void executeEvent(ActionItem event) {
		System.out.println("Event being executed: " + event.toString());
	}
}
