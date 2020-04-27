package president;

import java.awt.GraphicsEnvironment;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import president.gui.Regles;

public class President {

	public static void main(String[] args) {
		// Lance le jeu en double cliquant sur le jar
		lancer();
		
		Messages.afficher(Messages.BIENVENUE);
		afficherMenu();
	}
	
	@SuppressWarnings("resource")
	public static void lancer() {
		Console console = System.console();
	    if (console == null && !GraphicsEnvironment.isHeadless()) {
	    	String fileName = President.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
	    	try {
	    		File batch = new File("launcher.bat");
	    		if (!batch.exists()) {
	    			batch.createNewFile();
	    			PrintWriter writer = new PrintWriter(batch);
	    			writer.println("@echo off");
	    			writer.println("java -jar " + fileName);
	    			writer.println("exit");
	    			writer.flush();
	    		}
	    		Runtime.getRuntime().exec("cmd /c start \"\" " + batch.getPath());
	    	} catch(IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	}
	
	public static void afficherMenu() {
		System.out.println();
		Messages.afficher(Messages.MENU);
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
		
		afficherMenu();
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
