package president;

import java.util.Scanner;

public class President {

	public static void main(String[] args) {
		int choix = afficherMenu();
		
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
		}
		
		// TODO ajout des jokers avec methode choisirValeurJoker()
		// TODO règles du jeu
	}
	
	@SuppressWarnings("resource")
	private static int afficherMenu() {
		Messages.afficher(Messages.BIENVENUE);
		System.out.println();
		System.out.println("[0: Nouvelle partie] [1: Quitter]");
		System.out.println();
		
		// Tant que le choix n'est pas égal à 0 ou 1, saisir le choix
		int choix = 0;
		do {
			System.out.print("Choix : ");
			Scanner scanner = new Scanner(System.in);
			choix = scanner.nextInt(Character.MAX_RADIX);
			
			if (choix < 0 || choix > 1) {
				System.out.println(Messages.ERREUR_CHOIX);
			}
		} while (choix < 0 || choix > 1);
		
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
