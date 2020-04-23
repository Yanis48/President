package president;

public class Messages {
	public static final String BIENVENUE = "Bienvenue sur le jeu du Pr�sident !";
	public static final String QUITTER = "Vous avez quitt� le jeu du Pr�sident.";
	public static final String REGLES = "R�gles du Pr�sident";
	
	public static final String ERREUR_CHOIX = "[ERREUR] Le choix doit �tre �gal � 0, 1 ou 2 !";
	public static final String ERREUR_NOMBRE_PARTIES = "[ERREUR] Le nombre de parties doit �tre sup�rieur � 0 !";
	public static final String ERREUR_NOMBRE_JOUEURS = "[ERREUR] Le nombre de joueurs doit �tre compris entre 2 et 6 !";
	public static final String ERREUR_NOM_JOUEUR = "[ERREUR] Le nom du joueur doit �tre compris entre 1 et 8 caract�res !";
	public static final String ERREUR_NOM_PAS_UNIQUE = "[ERREUR] Le nom du joueur doit �tre unique !";
	public static final String ERREUR_MODE_CARTES = "[ERREUR] Le mode doit �tre compris entre 1 et 6 !";
	public static final String ERREUR_PAS_DANS_DECK = "[ERREUR] La carte choisie n'est pas dans le deck du joueur !";
	public static final String ERREUR_INFERIEURE = "[ERREUR] La carte doit �tre sup�rieure ou �gale � la pr�c�dente !";
	public static final String ERREUR_SUPERIEURE = "[ERREUR] La carte doit �tre inf�rieure ou �gale � la pr�c�dente !";
	public static final String ERREUR_PAS_EGALE = "[ERREUR] La carte choisie doit �tre �gale � la pr�c�dente !";
	public static final String ERREUR_VALEUR_JOKER = "[ERREUR] La valeur choisie doit �tre diff�rente du joker !";
	public static final String ERREUR_VALEUR_INVALIDE = "[ERREUR] La valeur choisie est invalide !";
	
	public static final String INFO_MELANGE = "[INFO] Le paquet a �t� m�lang�.";
	public static final String INFO_DISTRIBUE = "[INFO] Le paquet a �t� distribu�.";
	public static final String INFO_CARTES_DONNEES = "[INFO] %1s a donn� %1s carte(s) � %1s.";
	public static final String INFO_PILE_RESET = "[INFO] La pile a �t� r�initialis�e.";
	public static final String INFO_REVOLUTION_ON = "[INFO] La partie est maintenant en mode r�volution.";
	public static final String INFO_REVOLUTION_OFF = "[INFO] La partie n'est plus en mode r�volution.";
	public static final String INFO_TOUR_PASSE = "[INFO] %1s a pass� son tour.";
	public static final String INFO_FIN_JOUEUR = "[INFO] %1s a termin� la partie en �tant %1s.";
	public static final String INFO_FIN_PARTIE = "[INFO] La partie est termin�e !";
	public static final String INFO_ROLE_JOUEUR = "[INFO] %1s est %1s";
	
	public static final String MENU = "[0: Nouvelle partie] [1: Quitter] [2: R�gles]";
	public static final String PILE = "Pile";
	public static final String DECK_JOUEUR = "Deck de %1s";
	
	public static void afficher(String message, Object... args) {
		System.out.println(String.format(message, args));
	}
}
