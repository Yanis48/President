package president;

public class Messages {
	public static final String BIENVENUE = "Bienvenue sur le jeu du Président !";
	public static final String QUITTER = "Vous avez quitté le jeu du Président.";
	public static final String REGLES = "Règles du Président";
	
	public static final String ERREUR_CHOIX = "[ERREUR] Le choix doit être égal à 0, 1 ou 2 !";
	public static final String ERREUR_NOMBRE_PARTIES = "[ERREUR] Le nombre de parties doit être supérieur à 0 !";
	public static final String ERREUR_NOMBRE_JOUEURS = "[ERREUR] Le nombre de joueurs doit être compris entre 2 et 6 !";
	public static final String ERREUR_NOM_JOUEUR = "[ERREUR] Le nom du joueur doit être compris entre 1 et 8 caractères !";
	public static final String ERREUR_NOM_PAS_UNIQUE = "[ERREUR] Le nom du joueur doit être unique !";
	public static final String ERREUR_MODE_CARTES = "[ERREUR] Le mode doit être égal à 1, 2, 3 ou 4 !";
	public static final String ERREUR_PAS_DANS_DECK = "[ERREUR] La carte choisie n'est pas dans le deck du joueur !";
	public static final String ERREUR_INFERIEURE = "[ERREUR] La carte doit être supérieure ou égale à la précédente !";
	public static final String ERREUR_PAS_EGALE = "[ERREUR] La carte choisie doit être égale à la précédente !";
	
	public static final String INFO_MELANGE = "[INFO] Le paquet a été mélangé.";
	public static final String INFO_DISTRIBUE = "[INFO] Le paquet a été distribué.";
	public static final String INFO_CARTES_DONNEES = "[INFO] %1s a donné %1s carte(s) à %1s.";
	public static final String INFO_PILE_RESET = "[INFO] La pile a été réinitialisée.";
	public static final String INFO_TOUR_PASSE = "[INFO] %1s a passé son tour.";
	public static final String INFO_FIN_JOUEUR = "[INFO] %1s a terminé la partie en étant %1s.";
	public static final String INFO_FIN_PARTIE = "[INFO] La partie est terminée !";
	public static final String INFO_ROLE_JOUEUR = "[INFO] %1s est %1s";
	
	public static final String PILE = "Pile";
	public static final String DECK_JOUEUR = "Deck de %1s";
	
	public static void afficher(String message, Object... args) {
		System.out.println(String.format(message, args));
	}
}
