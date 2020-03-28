package president;

import president.carte.Carte;
import president.carte.Valeur;
import president.pile.Pile;

public class VerificateurCarte {
	private Carte carte;
	private Pile pile;
	private boolean valide = false;
	private boolean pileReset = false;
	private String messageErreur = null;
	
	public VerificateurCarte(Carte carte, Pile pile) {
		this.carte = carte;
		this.pile = pile;
	}
	
	public void verifier() {
		int prevCarteIndex = this.pile.getCartes().size() - 1;
		
		// V�rifier si la pile n'est pas vide
		if (this.pile.getCartes().size() > 0) {
			// V�rifier si la valeur de la carte est sup�rieure � la pr�c�dente
			if (this.carte.getValeur().compareTo(this.pile.getCartes().get(prevCarteIndex).getValeur()) > 0) {
				// V�rifier si les valeurs des 2 cartes pr�c�dentes ne sont pas �gales
				if (this.pile.getCartes().size() == 1 || !this.pile.getCartes().get(prevCarteIndex).getValeur().equals(this.pile.getCartes().get(prevCarteIndex - 1).getValeur())) {
					this.valide = true;
					this.verifierDeux();
				} else {
					this.messageErreur = Messages.ERREUR_PAS_EGALE;
				}
			// V�rifier si la valeur de la carte est �gale � la pr�c�dente
			} else if (this.carte.getValeur().compareTo(this.pile.getCartes().get(prevCarteIndex).getValeur()) == 0) {
				this.valide = true;
				this.verifierDeux();
				// V�rifier si les valeurs des 3 cartes pr�c�dentes sont �gales
				if (this.pile.getCartes().size() >= 3 && this.pile.getCartes().get(prevCarteIndex).getValeur().equals(this.pile.getCartes().get(prevCarteIndex - 1).getValeur()) && this.pile.getCartes().get(prevCarteIndex).getValeur().equals(this.pile.getCartes().get(prevCarteIndex - 2).getValeur())) {
					this.pileReset = true;
				}
			} else {
				this.messageErreur = Messages.ERREUR_INFERIEURE;
			}
		} else {
			this.valide = true;
			this.verifierDeux();
		}
	}
	
	/*
	 * V�rifie si la valeur de la carte est �gale � 2
	 * R�initialise la pile si la v�rification r�ussit
	 */
	private void verifierDeux() {
		if (this.carte.getValeur().equals(Valeur.DEUX)) {
			this.pileReset = true;
		}
	}
	
	public boolean isValide() {
		return this.valide;
	}
	
	public boolean isPileReset() {
		return this.pileReset;
	}
	
	public String getMessageErreur() {
		return this.messageErreur;
	}
}