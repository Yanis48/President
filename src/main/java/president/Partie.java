package president;

import java.util.ArrayList;
import java.util.Comparator;
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
	private Role[] roles;
	private int decksFinisPar2;
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
		
		// Initialisation des rôles à répartir
		this.roles = RepartitionRoles.REPARTITION_ROLES[this.getNombreJoueurs() - 2];
		
		// Mélange et distribution du paquet
		this.getPaquet().melanger();
		this.getPaquet().distribuer();
		
		// Détermination du joueur à avoir la main
		this.initPremierJoueur();
		
		if (!this.isPremierePartie()) {
			// Echange des cartes en fonction des rôles
			this.echangerCartes();
		}
		
		// Lancement des tours des joueurs
		this.toursJoueurs();
		
		// Finir la partie
		this.finirPartie();
	}
	
	@SuppressWarnings("resource")
	private void initNombreJoueurs() {
		System.out.print("Nombre de joueurs : ");
		Scanner scanner = new Scanner(System.in);
		int nombre = scanner.nextInt(Character.MAX_RADIX);
		
		if ((nombre < 2) || (nombre > 6)) {
			Messages.afficher(Messages.ERREUR_NOMBRE_JOUEURS);
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
			Messages.afficher(Messages.ERREUR_NOM_JOUEUR);
			return false;
		}
		return true;
	}
	
	private boolean isNomUnique(String nom) {
		for (Joueur joueur : joueurs) {
			if (joueur.getNom().equals(nom)) {
				Messages.afficher(Messages.ERREUR_NOM_PAS_UNIQUE);
				return false;
			}
		}
		return true;
	}
	
	private void initPremierJoueur() {
		joueurs.forEach(joueur -> {
			// Si c'est la première partie, le joueur qui possède la dame de coeur a la main
			if (this.isPremierePartie()) {
				Carte dameCoeur = new Carte(Valeur.DAME, Couleur.COEUR);
				if (joueur.getDeck().contient(dameCoeur)) {
					this.joueurMain = joueur;
				}
			// Sinon, le joueur ayant le rôle TROUDUC a la main
			} else {
				if (joueur.getRole().equals(Role.TROUDUC)) {
					this.joueurMain = joueur;
				}
			}
		});
	}
	
	private void echangerCartes() {
		Joueur president = null;
		Joueur vicePresident = null;
		Joueur viceTrouduc = null;
		Joueur trouduc = null;
		
		for (Joueur joueur : this.joueursPartie) {
			switch (joueur.getRole()) {
			case PRESIDENT:
				president = joueur;
				break;
			case VICE_PRESIDENT:
				vicePresident = joueur;
				break;
			case VICE_TROUDUC:
				viceTrouduc = joueur;
				break;
			case TROUDUC:
				trouduc = joueur;
				break;
			case NEUTRE:
			default:
				break;
			}
		}
		
		this.donnerMeilleuresCartes(trouduc, president, 2);
		this.choisirCartesADonner(president, trouduc, 2);
		if (this.getNombreJoueurs() >= 4) {
			this.donnerMeilleuresCartes(viceTrouduc, vicePresident, 1);
			this.choisirCartesADonner(vicePresident, viceTrouduc, 1);
		}
	}
	
	private void donnerMeilleuresCartes(Joueur donneur, Joueur receveur, int nombre) {
		for (int i = 0; i < nombre; i++) {
			// La meilleure carte est la dernière carte du deck
			int index = donneur.getDeck().getCartes().size() - 1;
			Carte carte = donneur.getDeck().getCarte(index);
			
			receveur.getDeck().ajouterCarte(carte);
			// Tri du deck du receveur
			receveur.getDeck().getCartes().sort(Comparator.comparing(Carte::getValeur));
			donneur.getDeck().enleverCarte(carte);
		}
		
		Messages.afficher(Messages.INFO_CARTES_DONNEES, donneur.getNom(), nombre, receveur.getNom());
	}
	
	@SuppressWarnings("resource")
	private void choisirCartesADonner(Joueur donneur, Joueur receveur, int nombre) {
		Scanner scanner = new Scanner(System.in);
		String valeurCarte;
		
		// Afficher le deck du donneur
		System.out.println();
		Messages.afficher(Messages.DECK_JOUEUR, donneur.getNom());
		donneur.getDeck().afficher();
		System.out.println();
		
		for (int i = 0; i < nombre; i++) {
			boolean found = false;
			
			System.out.println();
			System.out.print("Carte à donner : ");
			valeurCarte = scanner.nextLine();
			
			// Vérifier si la carte choisie est dans le deck du donneur
			for (Carte carte : donneur.getDeck().getCartes()) {
				if (carte.getValeur().getSymbole().equals(valeurCarte)) {
					
					receveur.getDeck().ajouterCarte(carte);
					// Tri du deck du receveur
					receveur.getDeck().getCartes().sort(Comparator.comparing(Carte::getValeur));
					donneur.getDeck().enleverCarte(carte);
					
					found = true;
					break;
				}
			}
			
			if (!found) {
				Messages.afficher(Messages.ERREUR_PAS_DANS_DECK);
				i--;
			}
		}
		
		Messages.afficher(Messages.INFO_CARTES_DONNEES, donneur.getNom(), nombre, receveur.getNom());
	}
	
	private void toursJoueurs() {
		int j = this.joueursPartie.indexOf(this.joueurMain);
		
		// Tant que la pile n'est pas vide, faire le tour des joueurs
		do {
			Joueur joueur = this.joueursPartie.get(j);
			
			// S'il ne reste qu'un seul joueur dans la partie
			if (this.joueursPartie.size() == 1) {
				Joueur dernierJoueur = this.joueursPartie.get(0);
				this.donnerRole(dernierJoueur);
				dernierJoueur.getDeck().reinitialiser();
				return;
			}
			
			// Choix et vérification de la carte à placer
			this.choisirCarte(joueur);
			
			// Si le joueur est toujours actif dans la partie
			if (this.joueursPartie.contains(joueur)) {
				// Si le joueur est le dernier de la liste, revenir au premier joueur
				if (j == this.joueursPartie.size() - 1) {
					j = 0;
				// Sinon, aller au joueur suivant
				} else {
					j++;
				}
			// Si le joueur n'est plus actif dans la partie
			} else {
				// Si le joueur a réinitialisé la pile, le joueur suivant a la main
				if (this.pile.isVide()) {
					this.joueurMain = (j == this.joueursPartie.size()) ? this.joueursPartie.get(0) : this.joueursPartie.get(j);
				} else if (j == this.joueursPartie.size()) {
					j = 0;
				}
			}
		} while (!this.pile.isVide());
		this.toursJoueurs();
	}
	
	@SuppressWarnings("resource")
	private void choisirCarte(Joueur joueur) {
		// Vérifier si tous les joueurs ont passé leur tour depuis la dernière carte posée
		if (this.nombreToursPasses == this.joueursPartie.size() - 1) {
			this.pile.reinitialiser();
			Messages.afficher(Messages.INFO_PILE_RESET);
		}
		System.out.println();
		
		// Si la pile n'est pas vide, afficher la pile
		if (!this.pile.isVide()) {
			Messages.afficher(Messages.PILE);
			this.pile.afficher();
			System.out.println();
			System.out.println();
		}
		
		// Afficher le deck du joueur
		Messages.afficher(Messages.DECK_JOUEUR, joueur.getNom());
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
				Messages.afficher(Messages.INFO_TOUR_PASSE, joueur.getNom());
			}
			
			Iterator<Carte> itCartes = joueur.getDeck().getCartes().iterator();
			
			int foundCount = 0;
			ArrayList<Carte> cartes = new ArrayList<Carte>();
			
			while (itCartes.hasNext() && !found) {
				carte = itCartes.next();
				
				if (carte.getValeur().getSymbole().equals(valeurCarte) || carte.getValeur().getSymbole().equals(Valeur.JOKER.getSymbole())) {
					foundCount += 1;
					cartes.add(carte);
					
					if (foundCount == this.mode.getId()) {
						VerificateurCarte verif = new VerificateurCarte(this.pile, this.mode, this.derniereCartePlacee, cartes);
						verif.verifier();
						
						// Si la carte peut être placée
						if (verif.isValide()) {
							found = true;
							this.derniereCartePlacee = carte;
							this.nombreToursPasses = 0;
							
							// Enlever les cartes du deck du joueur
							int cartesAEnlever = 0;
							for (Carte c : cartes) {
								if (cartesAEnlever <= this.mode.getId()) {
									// Si le deck du joueur contient la carte, l'enlever du deck
									if (joueur.getDeck().contient(c)) {
										joueur.getDeck().enleverCarte(c);
									// Sinon, enlever la dernière carte du deck, c'est-à-dire un joker
									} else {
										joueur.getDeck().enleverCarte(joueur.getDeck().getCarte(joueur.getDeck().getCartes().size() - 1));
									}
									cartesAEnlever++;
								}
							}
							
							// Ajouter les cartes dans la pile
							cartes.forEach(c -> this.pile.ajouterCarte(c));
							
							if (verif.isPileReset()) {
								this.pile.reinitialiser();
								Messages.afficher(Messages.INFO_PILE_RESET);
								this.joueurMain = joueur;
							}
						} else {
							messageErreur = verif.getMessageErreur();
						}
					}
				}
			}
			
			if (!found) {
				Messages.afficher(messageErreur);
			}
		}
		
		// Si le deck du joueur est vide, lui donner un rôle et le retirer des joueurs actifs
		if (joueur.getDeck().isVide()) {
			this.donnerRole(joueur);
			Messages.afficher(Messages.INFO_FIN_JOUEUR, joueur.getNom(), joueur.getRole());
			this.joueursPartie.remove(joueur);
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
		Messages.afficher(Messages.ERREUR_MODE_CARTES);
		return this.choisirMode();
	}
	
	private void donnerRole(Joueur joueur) {
		int joueursRestants = this.joueursPartie.size();
		int indexRole;
		
		// Si la dernière carte du joueur a pour valeur 2
		if (this.derniereCartePlacee.getValeur().equals(Valeur.DEUX)) {
			indexRole = this.getNombreJoueurs() - this.decksFinisPar2 - 1;
			this.decksFinisPar2++;
		} else {
			indexRole = this.getNombreJoueurs() - joueursRestants - this.decksFinisPar2;
		}
		
		joueur.setRole(this.roles[indexRole]);
		
		// Si le joueur est le premier à finir
		if (this.getNombreJoueurs() - joueursRestants == 0) {
			// Si la pile n'est pas déjà réinitialisée par la valeur de la carte
			if (!this.derniereCartePlacee.getValeur().equals(Valeur.DEUX)) {
				this.pile.reinitialiser();
				Messages.afficher(Messages.INFO_PILE_RESET);
			}
		}
	}
	
	private void finirPartie() {
		Messages.afficher(Messages.INFO_FIN_PARTIE);
		
		// Affichage des rôles des joueurs
		joueurs.forEach(joueur -> {
			Messages.afficher(Messages.INFO_ROLE_JOUEUR, joueur.getNom(), joueur.getRole());
		});
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
