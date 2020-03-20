package president.carte;

public class Carte {
	private Valeur valeur;
	private Couleur couleur;
	
	public Carte(Valeur valeur, Couleur couleur) {
		this.valeur = valeur;
		this.couleur = couleur;
	}
	
	public Valeur getValeur() {
		return this.valeur;
	}
	
	public Couleur getCouleur() {
		return this.couleur;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Carte other = (Carte) obj;
		if (couleur != other.couleur) return false;
		if (valeur != other.valeur) return false;
		return true;
	}
}
