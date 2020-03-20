package president.joueur;

import president.pile.Deck;

public class Joueur {
	private String nom;
	private Role role;
	private Deck deck;
	
	public Joueur(String nom) {
		this(nom, Role.NEUTRE);
	}
	
	public Joueur(String nom, Role role) {
		this.nom = nom;
		this.role = role;
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
