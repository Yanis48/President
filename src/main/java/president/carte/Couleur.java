package president.carte;

public enum Couleur {
	PIQUE("-PIQUE"), //♠
	COEUR("-COEUR"), //♥
	TREFLE("-TREFLE"), //♣
	CARREAU("-CARREAU"); //♦
	
	private String symbole;
	
	private Couleur(String symbole) {
		this.symbole = symbole;
	}
	
	public String getSymbole() {
		return this.symbole;
	}
}
