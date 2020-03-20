package president;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import president.carte.Carte;
import president.carte.Couleur;
import president.carte.Valeur;
import president.joueur.Joueur;
import president.joueur.Role;
import president.pile.Paquet;
import president.pile.Pile;

public class Partie {
	private int nombreJoueurs;
	private List<Joueur> joueurs = new ArrayList<Joueur>();
	private Paquet paquet;
	private Pile pile;
	private boolean premierePartie;
	private Joueur premierJoueur;
	
	public Partie(boolean premierePartie) {
		this.paquet = new Paquet(this);
		this.pile = new Pile();
		this.premierePartie = premierePartie;
	}
	
	@SuppressWarnings("resource")
	public void initNombreJoueurs() {
		System.out.print("Nombre de joueurs : ");
		Scanner scanner = new Scanner(System.in);
		int inNombreJoueurs = scanner.nextInt();
		
		if ((inNombreJoueurs < 2) || (inNombreJoueurs > 6)) {
			System.out.println(Messages.ERREUR_NOMBRE_JOUEURS);
			initNombreJoueurs();
		} else {
			this.nombreJoueurs = inNombreJoueurs;
		}
	}
	
	@SuppressWarnings("resource")
	public void initJoueurs() {		        
		Scanner scanner = new Scanner(System.in);
		String nom;
		
		for (int i = 0; i < this.nombreJoueurs; i++) {
			System.out.print("Nom du joueur " + (i + 1) + " : ");
			nom = scanner.nextLine();
			
			if (nom.length() <= 8) {
				Joueur joueur = new Joueur(nom);
				this.joueurs.add(joueur);
			} else {
				System.out.println(Messages.ERREUR_NOM_JOUEUR);
				i--;
			}
		}
	}
	
	public void initPremierJoueur() {
		this.joueurs.forEach(joueur -> {
			if (this.isPremierePartie()) {
				Carte dameCoeur = new Carte(Valeur.DAME, Couleur.COEUR);
				if (joueur.getDeck().contient(dameCoeur)) {
					this.premierJoueur = joueur;
				}
			} else {
				if (joueur.getRole().equals(Role.TROUDUC)) {
					this.premierJoueur = joueur;
				}
			}
		});
		
		System.out.println("Premier joueur : " + this.joueurs.indexOf(this.premierJoueur) + this.premierJoueur.getNom()); //debug
	}
	
	public void lancer() {
		int j = this.joueurs.indexOf(this.premierJoueur);
		while (true) {
			this.choisirCarte(this.joueurs.get(j));			
			if (j == this.joueurs.size() - 1) {
				j = 0;
			} else {
				j++;
			}
		}
	}
	
	@SuppressWarnings("resource")
	private void choisirCarte(Joueur joueur) {
		// Afficher la pile
		if (!this.pile.isVide()) {
			System.out.println("Pile");
			this.pile.afficher();
			System.out.println();
		}
		
		// Afficher le deck du joueur
		System.out.println("Deck de " + joueur.getNom());
		joueur.getDeck().afficher();
		System.out.println();
		
		// Saisir la carte à choisir
		Scanner scanner = new Scanner(System.in);
		
		Carte carte = null;
		boolean found = false;
		while (!found) {
			System.out.print("Carte choisie : ");
			String valeurCarte = scanner.nextLine();
			
			Iterator<Carte> itCartes = joueur.getDeck().getCartes().iterator();
			
			while (itCartes.hasNext() && !found) {
				carte = itCartes.next();
				if (carte.getValeur().getSymbole().equals(valeurCarte)) {
					if (this.peutEtrePlacee(carte)) {
						found = true;
					}
				}
			}
			
			if (!found) {
				System.out.println(Messages.ERREUR_PAS_DANS_DECK);
			}
		}
		
		// Enlever la carte du deck du joueur
		joueur.getDeck().enleverCarte(carte);
		
		// Ajouter la carte dans la pile
		this.pile.ajouterCarte(carte);
	}
	
	private boolean peutEtrePlacee(Carte carte) {
		boolean valide = false;
		int prevCarteIndex = this.pile.getCartes().size() - 1;
		
		// TODO passer son tour si joueur a vraiment pas la bonne carte
		// TODO reset la pile et nouveau joueur qui a la main si 4 cartes à la suite
		// TODO implémenter modes de jeu
		
		// Vérifier si la pile n'est pas vide
		if (this.pile.getCartes().size() > 0) {
			// Vérifier si la valeur de la carte est supérieure à la précédente
			if (carte.getValeur().compareTo(this.pile.getCartes().get(prevCarteIndex).getValeur()) > 0) {
				// Vérifier si les valeurs des 2 cartes précédentes ne sont pas égales
				if (this.pile.getCartes().size() <= 1 || !this.pile.getCartes().get(prevCarteIndex).getValeur().equals(this.pile.getCartes().get(prevCarteIndex - 1).getValeur())) {
					valide = true;
				}
			// Vérifier si la valeur de la carte est égale à la précédente
			} else if (carte.getValeur().compareTo(this.pile.getCartes().get(prevCarteIndex).getValeur()) == 0) {
				valide = true;
			}
		} else {
			valide = true;
		}
		
		return valide;
	}
	
	public int getNombreJoueurs() {
		return this.nombreJoueurs;
	}
	
	public List<Joueur> getJoueurs() {
		return this.joueurs;
	}
	
	public Joueur getJoueur(int index) {
		return this.joueurs.get(index);
	}
	
	public Paquet getPaquet() {
		return this.paquet;
	}
	
	public Pile getPile() {
		return this.pile;
	}
	
	public boolean isPremierePartie() {
		return this.premierePartie;
	}
}
