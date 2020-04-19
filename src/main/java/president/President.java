package president;

import java.util.Scanner;

public class President {

	public static void main(String[] args) {
		Messages.afficher(Messages.BIENVENUE);
		afficherMenu();
		
		// TODO ajout des jokers avec methode choisirValeurJoker()
		// TODO système de révolution
	}
	
	public static void afficherMenu() {
		System.out.println();
		System.out.println("[0: Nouvelle partie] [1: Quitter] [2: Règles]");
		System.out.println();
		
		int choix = choisirBoutonMenu();
		
		switch (choix) {
		case 0:
			int nombreParties = choisirNombreParties();
			
			for (int i = 0; i < nombreParties; i++) {
				// Seule la première itération est la première partie
				Partie partie = new Partie(i == 0);
				
				// Lancement de la partie
				partie.lancer();
			}
			break;
			
		case 1:
			Messages.afficher(Messages.QUITTER);
			System.exit(0);
			
		case 2:
			Regles.afficherRegles();
			break;
		}
	}
	
	@SuppressWarnings("resource")
	private static int choisirBoutonMenu() {
		int choix = 0;
		
		System.out.print("Choix : ");
		Scanner scanner = new Scanner(System.in);
		choix = scanner.nextInt(Character.MAX_RADIX);
		
		// Si le choix n'est pas égal à 0, 1 ou 2, recommencer la saisie
		if (choix < 0 || choix > 2) {
			Messages.afficher(Messages.ERREUR_CHOIX);
			return choisirBoutonMenu();
		}
		
		return choix;
	}
	
	@SuppressWarnings("resource")
	private static int choisirNombreParties() {
		System.out.print("Nombre de parties : ");
		Scanner scanner = new Scanner(System.in);
		int nombre = scanner.nextInt(Character.MAX_RADIX);
		
		if (nombre > 0) {
			System.out.println();
			return nombre;
		}
		
		Messages.afficher(Messages.ERREUR_NOMBRE_PARTIES);
		return choisirNombreParties();
	}
}
