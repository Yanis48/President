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
	private Joueur joueurMain;
	private Mode mode = Mode.SIMPLE;
	private Carte derniereCartePlacee;
	private int nombreToursPasses;
	
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
			this.initNombreJoueurs();
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
					this.joueurMain = joueur;
				}
			} else {
				if (joueur.getRole().equals(Role.TROUDUC)) {
					this.joueurMain = joueur;
				}
			}
		});
	}
	
	public void lancer() {
		int j = this.joueurs.indexOf(this.joueurMain);
		
		do {
			this.choisirCarte(this.joueurs.get(j));
			if (j == this.joueurs.size() - 1) {
				j = 0;
			} else {
				j++;
			}
		} while (!this.pile.isVide());
		this.lancer();
	}
	
	@SuppressWarnings("resource")
	private void choisirCarte(Joueur joueur) {
		// Vérifier si tous les joueurs ont passé leur tour depuis la dernière carte posée
		if (this.nombreToursPasses == this.joueurs.size() - 1) {
			this.pile.reinitialiser();
			System.out.println(Messages.INFO_PILE_RESET);
		}
		
		// Si la pile n'est pas vide, afficher la pile
		if (!this.pile.isVide()) {
			System.out.println();
			System.out.println("Pile");
			this.pile.afficher();
			System.out.println();
		}
		
		// Afficher le deck du joueur
		System.out.println("Deck de " + joueur.getNom());
		joueur.getDeck().afficher();
		System.out.println();
		
		// Si la pile est vide, choisir le mode
		if (this.pile.isVide()) {
			this.mode = this.choisirMode();
		}
		
		// Saisir la carte à choisir
		Scanner scanner = new Scanner(System.in);
		
		Carte carte = null;
		boolean found = false;
		int foundCount = 0;
		ArrayList<Carte> cartes = new ArrayList<Carte>();
		while (!found) {
			System.out.print("Carte choisie : ");
			String valeurCarte = scanner.nextLine();
			String messageErreur = Messages.ERREUR_PAS_DANS_DECK;
			
			// Vérifier si le joueur passe son tour
			if (valeurCarte.equals("0")) {
				found = true;
				this.derniereCartePlacee = null;
				this.nombreToursPasses += 1;
			}
			
			Iterator<Carte> itCartes = joueur.getDeck().getCartes().iterator();
			
			while (itCartes.hasNext() && !found) {
				carte = itCartes.next();
				if (carte.getValeur().getSymbole().equals(valeurCarte)) {
					foundCount += 1;
					cartes.add(carte);
					if (foundCount == this.mode.getId()) {
						VerificateurCarte verif = new VerificateurCarte(this.pile, this.mode, this.derniereCartePlacee, cartes);
						verif.verifier();
						if (verif.isValide()) {
							found = true;
							this.derniereCartePlacee = carte;
							this.nombreToursPasses = 0;
							
							// Enlever la carte du deck du joueur
							cartes.forEach(c -> joueur.getDeck().enleverCarte(c));
							
							// Ajouter la carte dans la pile
							cartes.forEach(c -> this.pile.ajouterCarte(c));
							
							if (verif.isPileReset()) {
								this.pile.reinitialiser();
								System.out.println(Messages.INFO_PILE_RESET);
								this.joueurMain = joueur;
							}
						} else {
							messageErreur = verif.getMessageErreur();
						}
					}
				}
			}
			
			if (!found) {
				System.out.println(messageErreur);
			}
		}
	}
	
	@SuppressWarnings("resource")
	private Mode choisirMode() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Mode choisi : ");
		int modeId = scanner.nextInt();
		
		// Vérifier si le mode choisi existe
		for (Mode mode : Mode.values()) {
			if (mode.getId() == modeId) {
				return mode;
			}
		}
		System.out.println(Messages.ERREUR_MODE_CARTES);
		return this.choisirMode();
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
