package president.pile;

import president.carte.Carte;

public class Deck extends Pile {
	
	public void enleverCarte(Carte carte) {
		this.cartes.remove(carte);
	}
	
	public boolean contient(Carte carte) {
		for (Carte carteDeck : this.cartes) {
			if (carte.equals(carteDeck)) {
				return true;
			}
		}
		return false;
	}
}
