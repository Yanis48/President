package president;

import java.util.ArrayList;
import java.util.Scanner;

import president.carte.Carte;
import president.carte.Couleur;
import president.carte.Valeur;
import president.pile.Pile;

public class VerificateurCarte {
	private Pile pile;
	private Mode mode;
	private Carte derniereCartePlacee;
	private ArrayList<Carte> cartes;
	private boolean valide = false;
	private boolean pileReset = false;
	private boolean revolution = false;
	private String messageErreur = null;
	
	public VerificateurCarte(Pile pile, Mode mode, Carte derniereCartePlacee, ArrayList<Carte> cartes) {
		this.pile = pile;
		this.mode = mode;
		this.derniereCartePlacee = derniereCartePlacee;
		this.cartes = cartes;
	}
	
	public void verifier(boolean revolutionEnCours) {
		int prevCarteIndex = this.pile.getCartes().size() - 1;
		
		this.verifierJoker();
		
		// Vérifier si la pile n'est pas vide
		if (this.pile.getCartes().size() > 0) {
			// Vérifier si la valeur de la carte est inférieure (en mode révolution) ou supérieure à la précédente
			if (revolutionEnCours ? this.isInferieure() : this.isSuperieure()) {
				// Vérifier si les valeurs des 2 cartes précédentes ne sont pas égales
				if (this.mode != Mode.SIMPLE || this.derniereCartePlacee == null || this.pile.getCartes().size() == 1 || !this.pile.getCartes().get(prevCarteIndex).getValeur().equals(this.pile.getCartes().get(prevCarteIndex - 1).getValeur())) {
					this.valide = true;
					if (revolutionEnCours) {
						this.verifierTrois();
					} else {
						this.verifierDeux();
					}
				} else {
					this.messageErreur = Messages.ERREUR_PAS_EGALE;
				}
			// Vérifier si la valeur de la carte est égale à la précédente
			} else if (this.isEgale()) {
				this.valide = true;
				
				// Vérifier si les valeurs des 3 cartes précédentes sont égales
				if (this.mode != Mode.SIMPLE || this.pile.getCartes().size() >= 3 && this.pile.getCartes().get(prevCarteIndex).getValeur().equals(this.pile.getCartes().get(prevCarteIndex - 1).getValeur()) && this.pile.getCartes().get(prevCarteIndex).getValeur().equals(this.pile.getCartes().get(prevCarteIndex - 2).getValeur())) {
					this.pileReset = true;
				}
			} else {
				this.messageErreur = revolutionEnCours ? Messages.ERREUR_SUPERIEURE : Messages.ERREUR_INFERIEURE;
			}
		} else {
			this.valide = true;
			if (revolutionEnCours) {
				this.verifierTrois();
			} else {
				this.verifierDeux();
			}
			this.verifierQuadruple();
		}
	}
	
	/*
	 * Vérifie si la valeur de la carte posée est inférieure à la valeur de la carte précédente
	 */
	private boolean isInferieure() {
		return this.cartes.get(0).getValeur().compareTo(this.pile.getCartes().get(this.pile.getCartes().size() - 1).getValeur()) < 0;
	}
	
	/*
	 * Vérifie si la valeur de la carte posée est supérieure à la valeur de la carte précédente
	 */
	private boolean isSuperieure() {
		return this.cartes.get(0).getValeur().compareTo(this.pile.getCartes().get(this.pile.getCartes().size() - 1).getValeur()) > 0;
	}
	
	/*
	 * Vérifie si la valeur de la carte posée est égale à la valeur de la carte précédente
	 */
	private boolean isEgale() {
		return this.cartes.get(0).getValeur().compareTo(this.pile.getCartes().get(this.pile.getCartes().size() - 1).getValeur()) == 0;
	}
	
	/*
	 * Vérifie si la valeur de la carte est égale à 2
	 * Réinitialise la pile en cas de succès
	 */
	private void verifierDeux() {
		if (this.cartes.get(0).getValeur().equals(Valeur.DEUX)) {
			this.pileReset = true;
		}
	}
	
	/*
	 * Vérifie si la valeur de la carte est égale à 3
	 * Réinitialise la pile en cas de succès
	 */
	private void verifierTrois() {
		if (this.cartes.get(0).getValeur().equals(Valeur.TROIS)) {
			this.pileReset = true;
		}
	}
	
	/*
	 * Vérifie si au moins 4 cartes sont posées en même temps
	 * Réinitialise la pile en cas de succès
	 */
	private void verifierQuadruple() {
		if (this.mode.equals(Mode.QUADRUPLE) || this.mode.equals(Mode.QUINTUPLE) || this.mode.equals(Mode.SEXTUPLE)) {
			this.pileReset = true;
			this.revolution = true;
		}
	}
	
	/*
	 * Vérifie si la valeur de la carte est un joker
	 * Permet de saisir la valeur choisie en cas de succès
	 */
	private void verifierJoker() {
		Carte carte = null;
		
		if (this.cartes.get(0).getValeur().equals(Valeur.JOKER)) {
			carte = new Carte(this.choisirValeurJoker(), Couleur.UNDEFINED);
		} else {
			carte = new Carte(this.cartes.get(0).getValeur(), Couleur.UNDEFINED);
		}
		
		for (Carte c : this.cartes) {
			if (c.getValeur().equals(Valeur.JOKER)) {
				this.cartes.set(this.cartes.indexOf(c), carte);
			}
		}
	}
	
	@SuppressWarnings("resource")
	private Valeur choisirValeurJoker() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Valeur choisie : ");
		String symbole = scanner.nextLine();
		
		// Vérifier si la valeur choisie existe
		for (Valeur valeur : Valeur.values()) {
			if (valeur.getSymbole().equals(symbole)) {
				
				// Vérifier si la valeur choisie n'est pas le joker
				if (!symbole.equals(Valeur.JOKER.getSymbole())) {
					return valeur;
				} else {
					Messages.afficher(Messages.ERREUR_VALEUR_JOKER);
					return this.choisirValeurJoker();
				}
			}
		}
		
		Messages.afficher(Messages.ERREUR_VALEUR_INVALIDE);
		return this.choisirValeurJoker();
	}
	
	public boolean isValide() {
		return this.valide;
	}
	
	public boolean isPileReset() {
		return this.pileReset;
	}
	
	public boolean isRevolution() {
		return this.revolution;
	}
	
	public String getMessageErreur() {
		return this.messageErreur;
	}
}
