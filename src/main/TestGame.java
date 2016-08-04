package main;

import java.util.HashMap;
import java.util.Scanner;

import beggars.Card;
import beggars.GameTracker;
import beggars.Player;
import beggars.Trick;

public class TestGame {
	
	private static GameTracker game;
	private static int currPlayer = -1;
	private static int firstToDecide = -1;
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
		whoGoesFirst();
		
		boolean playing = true;
		boolean leaster = waitForPickups();
		
		System.out.println(order[firstToDecide].getName() + " was the first to decide to pick up, he goes first.");
		
		currPlayer = firstToDecide;
		
		while (playing) {
			// play a new trick
			System.out.println("Starting a new trick!");
			Trick currTrick = new Trick(players.size());
			while (!currTrick.isOver()) {
				currTrick.addCard(order[currPlayer], getPlayerMove(currTrick));
				getUpNext();
			}
			currTrick.getWinningPlayer().takeTrick(currTrick);
			setWinnerAsCurrPlayer(currTrick.getWinningPlayer());
			if (order[0].getHand().isEmpty()) playing = false;
		}
		System.out.println("Game over!");
		
		// see everyone's points
		for (int i = 0; i < order.length; i++)
			order[i].printBanked();
	}
	
	public static void getUpNext() { 
		if (currPlayer == order.length - 1) currPlayer = 0;
		currPlayer++;
	}
	
	public static boolean askPlayerToPickUp(Player player) {
		String userInput = "";
		while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n")) {
			System.out.println(player.getName() + ", would you like to pick up the " + game.numDownCards() + " cards? ('hand' to see hand)");
			userInput = sc.nextLine();
			if (userInput.equalsIgnoreCase("hand")) player.printHand();
		}
		if (userInput.charAt(0) == 'y') return true;
		return false;
	}
	
	public static void setWinnerAsCurrPlayer(Player player) {
		for (int i = 0; i < order.length; i++)
			if (order[i].equals(player)) currPlayer = i;
	}
	
	public static Card getPlayerMove(Trick trick) {
		System.out.println(trick.toString());
		System.out.println(order[currPlayer].getName() + ", what card would you like to play?");
		order[currPlayer].printHand();
		int cardIndex = -1;
		Card currCard = null;
		while (cardIndex == -1) {
			while (!sc.hasNextInt()) sc.nextLine();
			cardIndex = sc.nextInt();
			try {
				currCard = order[currPlayer].getHand().get(cardIndex-1);
				if (!verifyMove(trick, currCard)) {
					System.out.println("You can't play " + currCard.toString() + " !!");
					cardIndex = -1;
				}
				// verify input
			} catch (Exception e) { System.out.println("Choose a valid index!"); cardIndex = -1; }
		}
		System.out.println(order[currPlayer].getName() + " played: " + currCard.toString());
		return order[currPlayer].playCard(cardIndex-1);
	}
	
	public static boolean verifyMove(Trick trick, Card card) {
		if (trick.isFirstMove()) return true; // first played card always valid
		if (trick.getPlayedSuit().equals(card.getSuit())) return true; // same suit always valid
		else // player is playing a different suit
			if (order[currPlayer].hasSuit(trick.getPlayedSuit())) return false; // re-nigger!
		return true;
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
	
	public static void whoGoesFirst() {
		String question = "Who is going first? (";
		String userInput;
		for (int i = 0; i < order.length; i++) {
			if (i != order.length - 1) question += order[i].getName() + ", ";
			else question += order[i].getName();
		}
		question += ")";
		while (currPlayer == -1) {
			System.out.println(question);
			userInput = sc.nextLine();
			for (int i = 0; i < order.length; i++)
				if (userInput.equalsIgnoreCase(order[i].getName())) {
					firstToDecide = i;
					currPlayer = i;
				}
		}
		System.out.println(order[currPlayer].getName() + " is choosing to pick up first!");
	}
	
	/*
	 * Returns whether or not the game is a leaster.
	 */
	public static boolean waitForPickups() {
		int chances = 0;
		while (!game.pickedUp() && chances < players.size()) {
			Player curr = order[currPlayer];
			if (askPlayerToPickUp(curr)) {
				curr.pickUp(game.getDownCards());
				havePlayerBankCards(curr);
			}
			else { chances++; getUpNext(); }
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
