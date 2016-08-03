package beggars;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
	private List<Card> hand;
	private List<Card> banked;
	private List<Trick> tricksWon;
	private String name;
	private int totalPoints; // for keeping track of who has won each "match"
	private int sizeOfPickup = -1;
	private boolean pickedUp = false;
	private boolean partnerCard = false;
	
	public Player(String name) {
		this.name = name;
		totalPoints = 0;
		tricksWon = new ArrayList<Trick>();
		banked = new ArrayList<Card>();
	}
	
	public String getName() { return name; }
	
	public void setDeck(List<Card> cards) {
		this.hand = cards;
		
		// checks if you got the partner card
		Iterator<Card> itr = cards.iterator();
		while (itr.hasNext())
			if (itr.next().isPartnerCard()) partnerCard = true;
	}
	
	public int getHandPoints() { return getPoints(hand); }
	public int getBankPoints() { return getPoints(banked); }
	
	private int getPoints(List<Card> cards) {
		int totalPoints = 0;
		Iterator<Card> itr = cards.iterator();
		while (itr.hasNext()) totalPoints += itr.next().getPoints();
		return totalPoints;
	}
	
	public void pickUp(List<Card> cards) {
		hand.addAll(cards);
		sizeOfPickup = cards.size();
		pickedUp = true;
	}
	
	public void bank(int index) { banked.add(hand.remove(index)); }
	
	public int getSizeOfPickup() { return sizeOfPickup; }
	
	public boolean pickedUp() { return pickedUp; }
	
	public int numCards() { return hand.size(); }
	
	public void takeTrick(Trick trick) { 
		tricksWon.add(trick);
		banked.addAll(trick.getCards());
	}
	
	public boolean isPartner() { return partnerCard; }
	
	public boolean playCard(Card card) { return hand.remove(card); }
	
	public boolean hasRed() { return hasSuit(Suit.RED); }
	public boolean hasBlack() { return hasSuit(Suit.BLACK); }
	public boolean hasGreen() { return hasSuit(Suit.GREEN); }
	public boolean hasBlue() { return hasSuit(Suit.BLUE); }
	
	public boolean hasSuit(Suit suit) {
		Iterator<Card> itr = hand.iterator();
		while (itr.hasNext())
			if (itr.next().getSuit().equals(suit)) return true;
		return false;
	}
	
	public void printHand() {
		String youPickedUp, partner;
		
		if (pickedUp) youPickedUp = "Yes";
		else youPickedUp = "No";
		if (partnerCard) partner = "Yes";
		else partner = "No";
		
		System.out.println("----------------------------------------");
		System.out.println(name + "'s hand: ");
		System.out.println("----------------------------------------");
		System.out.println("Cards: "+hand.size()+", Points: "+this.getHandPoints());
		System.out.println("Did you pick up? ("+youPickedUp+")");
		System.out.println("Are you the partner? ("+partner+")\n");
		Iterator<Card> itr = hand.iterator();
		int index = 1;
		while (itr.hasNext()) {
			System.out.println("("+index+") "+itr.next().toString());
			index++;
		}
		System.out.println("========================================");
	}
	
	public void printBanked() {
		System.out.println("----------------------------------------");
		System.out.println(name + "'s bank: ");
		System.out.println("----------------------------------------");
		System.out.println("Cards: "+banked.size()+", Points: "+this.getBankPoints());
		Iterator<Card> itr = banked.iterator();
		while (itr.hasNext()) 
			System.out.println(itr.next().toString());
		System.out.println("========================================");
	}
}
