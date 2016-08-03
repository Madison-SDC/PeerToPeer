package beggars;

public class Card {
	
	private Suit suit;
	private int value;
	private int points;
	private boolean partnerCard = false;
	
	public Card(Suit suit, int value, int points) {
		this.suit = suit;
		this.value = value;
		this.points = points;
		if (value == 6 && suit.equals(Suit.BLACK)) partnerCard = true;
	}
	
	public Suit getSuit() { return suit; }
	public int getValue() { return value; }
	public int getPoints() { return points; }
	public boolean isPartnerCard() { return partnerCard; }
	
	public boolean equals(Card otherCard) {
		if (value == otherCard.getValue() && suit.equals(otherCard.getSuit())) return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "Suit: " + suit.toString() + ", Value: " + value + ", Points: " + points;
	}
}
