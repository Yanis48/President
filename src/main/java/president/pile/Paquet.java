package president.pile;

import java.util.Collections;
import java.util.Comparator;

import president.Messages;
import president.Partie;
import president.carte.Carte;
import president.carte.Couleur;
import president.carte.Valeur;

public class Paquet extends Pile {
	private static final int NOMBRE_CARTES = 54;
	private Partie partie;
	
	public Paquet(Partie partie) {
		this.partie = partie;
		
		for (Valeur valeur : Valeur.values()) {
			for (Couleur couleur : Couleur.values()) {
				// Si la valeur est différente de JOKER et la couleur est différente de UNDEFINED
				if (!valeur.equals(Valeur.JOKER) && !couleur.equals(Couleur.UNDEFINED)) {
					Carte carte = new Carte(valeur, couleur);
					this.ajouterCarte(carte);
				}
			}
		}
		
		// Ajout de 2 jokers dans le paquet
		Carte joker = new Carte(Valeur.JOKER, Couleur.UNDEFINED);
		this.ajouterCarte(joker);
		this.ajouterCarte(joker);
	}
	
	public void melanger() {
		Collections.shuffle(this.cartes);
		Messages.afficher(Messages.INFO_MELANGE);
	}
	
	public void distribuer() {
		int nombreJoueurs = this.partie.getNombreJoueurs();
		int nombreCartesParJoueur = (int) Math.floor(NOMBRE_CARTES / nombreJoueurs);
		
		// Si le nombre de cartes par joueur ne tombe pas rond, on donne une carte en plus
		nombreCartesParJoueur += (NOMBRE_CARTES % nombreJoueurs != 0) ? 1 : 0;
		
		for (int carte = 0; carte < nombreCartesParJoueur; carte++) {
			for (int joueur = 0; joueur < nombreJoueurs; joueur++) {
				Deck deck = this.partie.getJoueurs().get(joueur).getDeck();
				int index = joueur + (nombreJoueurs * carte);
				
				if (index < NOMBRE_CARTES) {
					deck.ajouterCarte(this.cartes.get(index));
				}
				
				// Tri du deck par valeur
				deck.getCartes().sort(Comparator.comparing(Carte::getValeur));
			}
		}
		
		Messages.afficher(Messages.INFO_DISTRIBUE);
	}
}
