package beggars;

public enum Suit { 
	
	GREEN, BLACK, RED, BLUE;
	
	@Override
	public String toString() {
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}

}
