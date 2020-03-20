package president.pile;

import java.util.Collections;
import java.util.Comparator;

import president.Messages;
import president.Partie;
import president.carte.Carte;
import president.carte.Couleur;
import president.carte.Valeur;
import president.joueur.Joueur;

public class Paquet extends Pile {
	private static final int NOMBRE_CARTES = 52;
	private Partie partie;
	
	public Paquet(Partie partie) {
		this.partie = partie;
		
		for (Valeur valeur : Valeur.values()) {
			for (Couleur couleur : Couleur.values()) {
				Carte carte = new Carte(valeur, couleur);
				this.ajouterCarte(carte);
			}
		}
	}
	
	public void melanger() {
		Collections.shuffle(this.cartes);
		System.out.println(Messages.INFO_MELANGE);
		
		/*
		 * DEBUG
		 */
		for (Carte carte : this.cartes) {
			System.out.println(carte.getValeur() + " - " + carte.getCouleur());
		}
	}
	
	public void distribuer() {
		int nombreJoueurs = this.partie.getNombreJoueurs();
		int nombreCartesParJoueur = (int) Math.floor(NOMBRE_CARTES / nombreJoueurs);
		
		// Si le nombre de cartes par joueur ne tombe pas rond, on donne une carte en plus
		nombreCartesParJoueur += (NOMBRE_CARTES % nombreJoueurs != 0) ? 1 : 0;
		
		for (int joueur = 0; joueur < nombreJoueurs; joueur++) {
			Deck deck = this.partie.getJoueurs().get(joueur).getDeck();
			
			for (int carte = 0; carte < nombreCartesParJoueur; carte++) {
				int index = carte + (nombreCartesParJoueur * joueur);
				if (index < NOMBRE_CARTES) {
					deck.ajouterCarte(this.cartes.get(index));
				}
			}
			
			// Tri du deck par valeur
			deck.getCartes().sort(Comparator.comparing(Carte::getValeur));
		}
		System.out.println(Messages.INFO_DISTRIBUE);
		
		/*
		 * DEBUG
		 */
		for (Joueur joueur : this.partie.getJoueurs()) {
			joueur.getDeck().afficher();
			System.out.println();
		}
	}
}
