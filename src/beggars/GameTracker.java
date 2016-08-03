package beggars;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameTracker {
	
	private Deck deck;
	private Map<String, Player> players;
	private List<Card> downCards;
	private boolean pickedUp = false;

	public GameTracker(Map<String, Player> players) {
		if (players.size() < 3 || players.size() > 5)
			throw new IllegalArgumentException("Must play with 3-5 players!");
		
		this.players = players;
		deck = new Deck();
		
		downCards = deck.distributeCards(players);	
	}
	
	public void printDownCards() {
		System.out.println("----------------------------------------");
		System.out.println("Down cards: ");
		System.out.println("----------------------------------------");
		Iterator<Card> itr = downCards.iterator();
		while (itr.hasNext()) System.out.println(itr.next().toString());
	}
	
	public Player getPlayer(String name) { return players.get(name); }
	
	public List<Card> getDownCards() {
		pickedUp = true;
		return downCards;
	}
	
	public int numDownCards() { return downCards.size(); }
	
	public boolean pickedUp() { return pickedUp; }
}
