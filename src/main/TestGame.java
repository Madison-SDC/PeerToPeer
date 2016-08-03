package main;

import java.util.HashMap;
import java.util.Scanner;

import beggars.GameTracker;
import beggars.Player;
import beggars.Trick;

public class TestGame {
	
	private static GameTracker game;
	private static int currPlayer = 0;
	private static Player[] order;
	private static HashMap<String, Player> players;
	private static Scanner sc = new Scanner(System.in);	

	public static void main(String[] args) {
		
		players = new HashMap<String,Player>();
		players.put("Peter", new Player("Peter"));
		players.put("Vaughn", new Player("Vaughn"));
		players.put("Brian", new Player("Brian"));
		
		order = new Player[]{
				players.get("Peter"),
				players.get("Vaughn"),
				players.get("Brian")
		};
		
		game = new GameTracker(players);
		//game.printDownCards();
		//testPrintHands();
		
		boolean playing = true;
		boolean leaster = false;
		while (playing) {
			
			leaster = waitForPickups();
			
			// play a new trick
			//Trick currTrick = new Trick(players.size());
			//while (currTrick.isOver()) {
				
			//}
			playing = false;
		}
		System.out.println("Game over!");
	}
	
	public static Player getUpNext() { 
		if (currPlayer == order.length) currPlayer = 0;
		Player temp = order[currPlayer];
		currPlayer++;
		return temp;
	}
	
	public static boolean askPlayerToPickUp(Player player) {
		String userInput = "";
		while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n")) {
			System.out.println(player.getName()+", would you like to pick up the " + game.numDownCards() + " cards? ('hand' to see hand)");
			userInput = sc.nextLine();
			if (userInput.equalsIgnoreCase("hand")) player.printHand();
		}
		if (userInput.charAt(0) == 'y') return true;
		return false;
	}
	
	public static void havePlayerBankCards(Player player) {
		int cardIndex = -1;
		int remaining = player.getSizeOfPickup();
		while (remaining > 0) {
			player.printHand();
			player.printBanked();
			System.out.println("\nWhich card(s) would you like to bank? ("+remaining+" remaining)\n");
			while (!sc.hasNextInt()) sc.nextLine();
			cardIndex = sc.nextInt();
			try {
				player.bank(cardIndex-1);
				remaining--;
			} catch (IndexOutOfBoundsException e) { System.out.println("Choose a valid index!"); }
		}
	}
	
	/*
	 * Returns whether or not the game is a leaster.
	 */
	public static boolean waitForPickups() {
		System.out.println(order[currPlayer].getName() + " is choosing first!");
		int chances = 0;
		while (!game.pickedUp() && chances < players.size()) {
			Player currPlayer = getUpNext();
			if (askPlayerToPickUp(currPlayer)) {
				currPlayer.pickUp(game.getDownCards());
				havePlayerBankCards(currPlayer);
			}
			else chances++;
		}
		if (!game.pickedUp() && chances == players.size()) {
			System.out.println("Leaster!");
			return true;
		}
		return false;
	}
	
	public static void testPrintHands() {
		game.getPlayer("Peter").printHand();	
		game.getPlayer("Vaughn").printHand();
		game.getPlayer("Brian").printHand();
	}
}
