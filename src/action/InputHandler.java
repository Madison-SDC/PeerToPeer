package action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Polls System.in, handles routing to different connections
 */
public class InputHandler extends Thread implements Runnable {
	
	private LinkedBlockingQueue<ActionItem> userInput;
	private BufferedReader stdIn;
	private String userStringInput;
	
	public InputHandler(LinkedBlockingQueue<ActionItem> queue) {
		super();
		userInput = queue;
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		this.start();
	}
	
	public void run() {
		try {
			while ((userStringInput = stdIn.readLine()) != null) processInput(userStringInput);
		} catch (IOException io) { io.printStackTrace(); }
	}
	
	private void processInput(String in) {
		System.out.println("Processing: " + in);
		try {
		userInput.put(new ActionItem(in));
		} catch (InterruptedException ie) { ie.printStackTrace(); }
	}
}
