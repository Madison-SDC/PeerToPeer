package beggars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Trick {
	
	private List<Card> cards;
	private Map<Card, Player> cardToPlayer;
	private int numPlayers = 0;
	private Suit playedSuit = null;
	
	public Trick(int numPlayers) {
		cardToPlayer = new HashMap<Card, Player>();
		cards = new ArrayList<Card>();
		this.numPlayers = numPlayers;
	}
	
	public List<Card> getCards() { return cards; }
	
	public boolean isOver() { return cards.size() == numPlayers; }
	
	/*
	 * Indicates whether last played card ended the trick.
	 * 
	 * True: trick is over
	 * 
	 * False: trick is not over
	 */
	public boolean addCard(Player player, Card card) {
		// first card
		if (cards.size() == 0) playedSuit = card.getSuit();
		cardToPlayer.put(card, player);
		cards.add(card);
		if (this.isOver()) return true;
		return false;
	}
	
	/*
	 * Returns the player that won the trick, if the trick is over.
	 */
	public Player getWinningPlayer() { return cardToPlayer.get(this.getWinningCard()); }
	
	public Card getWinningCard() {
		Card temp = null;
		temp = this.checkHighestOfSuit(Suit.BLACK);
		if (temp == null) return checkHighestOfSuit(playedSuit);
		return temp;
	}
	
	/*
	 * Will return the highest card of any suit, if one is present.
	 */
	private Card checkHighestOfSuit(Suit suit) {
		Card temp = null;
		Iterator<Card> itr = cards.iterator();
		while (itr.hasNext()) {
			Card curr = itr.next();
			if (curr.getSuit().equals(suit))
				if (temp != null && curr.getValue() > temp.getValue()) temp = curr;
		}
		return temp;
	}
	
	@Override
	public String toString() {
		if (cards.isEmpty()) return "No one has played a card yet!";
		
		String retval = "----------------------------------------\n";
		retval += "Current Trick\n";
		retval += "----------------------------------------\n";
		
		for (Entry<Card, Player> entry : cardToPlayer.entrySet()) {
			Player currPlayer = entry.getValue();
			Card currCard = entry.getKey();
			retval += "("+currPlayer.getName()+"): " + currCard.toString();
			if (currCard.equals(this.getWinningCard())) retval += " **WILL WIN**";
			retval += "\n";
		}
		return retval;
	}
 }
