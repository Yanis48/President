package president.joueur;

import president.pile.Deck;

public class Joueur {
	private String nom;
	private Role role;
	private Deck deck;
	
	public Joueur(String nom) {
		this.nom = nom;
		this.role = Role.NEUTRE;
		this.deck = new Deck();
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public Role getRole() {
		return this.role;
	}
	
	public Deck getDeck() {
		return this.deck;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
}
