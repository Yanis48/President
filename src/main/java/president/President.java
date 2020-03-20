package president;

public class President {

	public static void main(String[] args) {
		Partie partie = new Partie(true);
		
		/*
		 * Initialisation de la partie
		 */
		partie.initNombreJoueurs();
		partie.initJoueurs();
		partie.getPaquet().melanger();
		partie.getPaquet().distribuer();
		partie.initPremierJoueur();
		
		/*
		 * Lancement de la partie
		 */
		partie.lancer();
	}
}
