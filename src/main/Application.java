package main;

import network.ConnectionManager;

public class Application {
	
	private static ConnectionManager cm;
	
	public static void main(String[] args) {
		cm = new ConnectionManager();
	}
}
