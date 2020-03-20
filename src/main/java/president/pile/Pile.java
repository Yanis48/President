package president.pile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import president.carte.Carte;

public class Pile {
	protected List<Carte> cartes = new ArrayList<Carte>();
	
	public Pile() {
		Arrays.fill(cartes.toArray(), null);
	}
	
	public List<Carte> getCartes() {
		return this.cartes;
	}
	
	public Carte getCarte(int index) {
		return this.cartes.get(index);
	}
	
	public void ajouterCarte(Carte carte) {
		this.cartes.add(carte);
	}
	
	public void afficher() {
		for (Carte carte : this.cartes) {
			System.out.print("[" + carte.getValeur().getSymbole() + carte.getCouleur().getSymbole() + "] ");
		}
		System.out.println();
	}
	
	public void afficherCarte(int index) {
		System.out.print(this.cartes.get(index));
	}
	
	public void reinitialiser() {
		this.cartes.clear();
	}
	
	public boolean isVide() {
		return this.cartes.isEmpty();
	}
}
