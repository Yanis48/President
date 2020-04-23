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
		
		// V�rifier si la pile n'est pas vide
		if (this.pile.getCartes().size() > 0) {
			// V�rifier si la valeur de la carte est inf�rieure (en mode r�volution) ou sup�rieure � la pr�c�dente
			if (revolutionEnCours ? this.isInferieure() : this.isSuperieure()) {
				// V�rifier si les valeurs des 2 cartes pr�c�dentes ne sont pas �gales
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
			// V�rifier si la valeur de la carte est �gale � la pr�c�dente
			} else if (this.isEgale()) {
				this.valide = true;
				
				// V�rifier si les valeurs des 3 cartes pr�c�dentes sont �gales
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
	 * V�rifie si la valeur de la carte pos�e est inf�rieure � la valeur de la carte pr�c�dente
	 */
	private boolean isInferieure() {
		return this.cartes.get(0).getValeur().compareTo(this.pile.getCartes().get(this.pile.getCartes().size() - 1).getValeur()) < 0;
	}
	
	/*
	 * V�rifie si la valeur de la carte pos�e est sup�rieure � la valeur de la carte pr�c�dente
	 */
	private boolean isSuperieure() {
		return this.cartes.get(0).getValeur().compareTo(this.pile.getCartes().get(this.pile.getCartes().size() - 1).getValeur()) > 0;
	}
	
	/*
	 * V�rifie si la valeur de la carte pos�e est �gale � la valeur de la carte pr�c�dente
	 */
	private boolean isEgale() {
		return this.cartes.get(0).getValeur().compareTo(this.pile.getCartes().get(this.pile.getCartes().size() - 1).getValeur()) == 0;
	}
	
	/*
	 * V�rifie si la valeur de la carte est �gale � 2
	 * R�initialise la pile en cas de succ�s
	 */
	private void verifierDeux() {
		if (this.cartes.get(0).getValeur().equals(Valeur.DEUX)) {
			this.pileReset = true;
		}
	}
	
	/*
	 * V�rifie si la valeur de la carte est �gale � 3
	 * R�initialise la pile en cas de succ�s
	 */
	private void verifierTrois() {
		if (this.cartes.get(0).getValeur().equals(Valeur.TROIS)) {
			this.pileReset = true;
		}
	}
	
	/*
	 * V�rifie si au moins 4 cartes sont pos�es en m�me temps
	 * R�initialise la pile en cas de succ�s
	 */
	private void verifierQuadruple() {
		if (this.mode.equals(Mode.QUADRUPLE) || this.mode.equals(Mode.QUINTUPLE) || this.mode.equals(Mode.SEXTUPLE)) {
			this.pileReset = true;
			this.revolution = true;
		}
	}
	
	/*
	 * V�rifie si la valeur de la carte est un joker
	 * Permet de saisir la valeur choisie en cas de succ�s
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
		
		// V�rifier si la valeur choisie existe
		for (Valeur valeur : Valeur.values()) {
			if (valeur.getSymbole().equals(symbole)) {
				
				// V�rifier si la valeur choisie n'est pas le joker
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
