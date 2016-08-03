package beggars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * Black: 1, 2, 3, 4, 5 (1), 6 (4), 7 (10), 8 (11), 9 (2), 10 (2), 11 (3), 12 (3), 13 (3), 14 (3)
 * Red: 1, 2, 3, 4, 5 (1), 6 (4), 7 (10), 8 (11)
 * Green: 1, 2, 3, 4, 5 (1), 6 (4), 7 (10), 8 (11)
 * Blue: 1, 2, 3, 4, 5 (1), 6 (4), 7 (10), 8 (11)
 * 
 * 38 cards
 * 3 players: 11 cards each, 5 card pickup
 * 4 players: 9 cards each, 2 card pickup?
 * 5 players: 7 cards each, 3 card pickup
 */
public class Deck {
	
	List<Card> deck;
	
	public Deck() {
		deck = new ArrayList<Card>();
		int currPoints = 0;
		
		// Initialize deck
		for (int i = 1; i < 9; i++) {
			switch (i) {
			case 5: currPoints = 1; break;
			case 6: currPoints = 4; break;
			case 7: currPoints = 10; break;
			case 8: currPoints = 11; break;
			default: currPoints = 0;
			}
			deck.add(new Card(Suit.RED, i, currPoints));
			deck.add(new Card(Suit.BLACK, i, currPoints));
			deck.add(new Card(Suit.BLUE, i, currPoints));
			deck.add(new Card(Suit.GREEN, i, currPoints));
		}
		for (int i = 9; i < 15; i++) {
			switch (i) {
			case 9:
			case 10: currPoints = 2; break;
			default: currPoints = 3; break;
			}
			deck.add(new Card(Suit.BLACK, i, currPoints));
		}
		
		// shuffle the deck
		Collections.shuffle(deck);
	}
	
	/*
	 * Adds cards to each players hand, returns the cards that remain
	 */
	public List<Card> distributeCards(Map<String, Player> players) {
		int cardsToDeal = 0;
		switch (players.size()) {
		case 3: cardsToDeal = 11; break;
		case 4: cardsToDeal = 9; break;
		case 5: cardsToDeal = 7; break;
		}
		
		// Go through each player
		for (Entry<String, Player> entry : players.entrySet()) {
			Player currPlayer = entry.getValue();
			ArrayList<Card> newHand = new ArrayList<Card>();
			for (int i = 0; i < cardsToDeal; i++)
				newHand.add(deck.remove(0)); // take from the top since we already shuffled
			currPlayer.setDeck(newHand);
		}
		
		// whatever's left can be picked up
		return deck;
	}
}
