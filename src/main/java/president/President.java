package president;

import java.util.Scanner;

public class President {

	public static void main(String[] args) {
		int choix = afficherMenu();
		
		switch (choix) {
		case 0:
			Partie partie = new Partie(true);
			
			// Lancement de la partie
			partie.lancer();
			break;
		case 1:
			System.out.println(Messages.QUITTER);
			System.exit(0);
		}
		
		// TODO si un joueur finit par un 2, il est trouduc
		// TODO ajout des jokers avec methode choisirValeurJoker()
		// TODO support de plusieurs parties à la suite
		// TODO règles du jeu
	}
	
	@SuppressWarnings("resource")
	private static int afficherMenu() {
		System.out.println(Messages.BIENVENUE);
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
}
