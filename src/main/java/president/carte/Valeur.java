package president.carte;

public enum Valeur {
	TROIS("3"),
	QUATRE("4"),
	CINQ("5"),
	SIX("6"),
	SEPT("7"),
	HUIT("8"),
	NEUF("9"),
	DIX("10"),
	VALET("V"),
	DAME("D"),
	ROI("R"),
	AS("A"),
	DEUX("2");
	
	private String symbole;
	
	private Valeur(String symbole) {
		this.symbole = symbole;
	}
	
	public String getSymbole() {
		return this.symbole;
	}
}
