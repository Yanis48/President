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
	private static int nombreJoueurs;
	private static List<Joueur> joueurs = new ArrayList<Joueur>();
	private List<Joueur> joueursPartie = new ArrayList<Joueur>();
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
	
	public void lancer() {
		if (this.isPremierePartie()) {
			// Initialisation du nombre de joueurs
			this.initNombreJoueurs();
			// Initialisation des noms des joueurs
			this.initJoueurs();
		}
		// Copie de tous les joueurs dans les joueurs actifs de la partie
		this.joueursPartie.addAll(joueurs);
		
		// Mélange et distribution du paquet
		this.getPaquet().melanger();
		this.getPaquet().distribuer();
		
		// Détermination du joueur à avoir la main
		this.initPremierJoueur();
		
		// Lancement des tours des joueurs
		this.toursJoueurs();
	}
	
	@SuppressWarnings("resource")
	private void initNombreJoueurs() {
		System.out.print("Nombre de joueurs : ");
		Scanner scanner = new Scanner(System.in);
		int nombre = scanner.nextInt();
		
		if ((nombre < 2) || (nombre > 6)) {
			System.out.println(Messages.ERREUR_NOMBRE_JOUEURS);
			this.initNombreJoueurs();
		} else {
			nombreJoueurs = nombre;
		}
	}
	
	@SuppressWarnings("resource")
	private void initJoueurs() {		        
		Scanner scanner = new Scanner(System.in);
		String nom;
		
		for (int i = 0; i < nombreJoueurs; i++) {
			System.out.print("Nom du joueur " + (i + 1) + " : ");
			nom = scanner.nextLine();
			
			if (this.isNomValide(nom) && this.isNomUnique(nom)) {
				Joueur joueur = new Joueur(nom);
				joueurs.add(joueur);
			} else {
				i--;
			}
		}
	}
	
	private boolean isNomValide(String nom) {
		if (nom.length() > 8 || nom.isBlank()) {
			System.out.println(Messages.ERREUR_NOM_JOUEUR);
			return false;
		}
		return true;
	}
	
	private boolean isNomUnique(String nom) {
		for (Joueur joueur : joueurs) {
			if (joueur.getNom().equals(nom)) {
				System.out.println(Messages.ERREUR_NOM_PAS_UNIQUE);
				return false;
			}
		}
		return true;
	}
	
	private void initPremierJoueur() {
		joueurs.forEach(joueur -> {
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
	
	private void toursJoueurs() {
		int j = this.joueursPartie.indexOf(this.joueurMain);
		
		do {
			this.choisirCarte(joueurs.get(j));
			if (j == this.joueursPartie.size() - 1) {
				j = 0;
			} else {
				j++;
			}
		} while (!this.pile.isVide());
		this.toursJoueurs();
	}
	
	@SuppressWarnings("resource")
	private void choisirCarte(Joueur joueur) {
		// Vérifier si tous les joueurs ont passé leur tour depuis la dernière carte posée
		if (this.nombreToursPasses == this.joueursPartie.size() - 1) {
			this.pile.reinitialiser();
			System.out.println(Messages.INFO_PILE_RESET);
		}
		System.out.println();
		
		// Si la pile n'est pas vide, afficher la pile
		if (!this.pile.isVide()) {
			System.out.println("Pile");
			this.pile.afficher();
			System.out.println();
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
			
			int foundCount = 0;
			ArrayList<Carte> cartes = new ArrayList<Carte>();
			
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
		int modeId = scanner.nextInt(Character.MAX_RADIX);
		
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
		return nombreJoueurs;
	}
	
	public List<Joueur> getJoueurs() {
		return joueurs;
	}
	
	public Joueur getJoueur(int index) {
		return joueurs.get(index);
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
