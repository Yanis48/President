package president;

import president.joueur.Role;

public class RepartitionRoles {	
	private static final Role[] DEUX_JOUEURS = {Role.PRESIDENT, Role.TROUDUC};
	private static final Role[] TROIS_JOUEURS = {Role.PRESIDENT, Role.NEUTRE, Role.TROUDUC};
	private static final Role[] QUATRE_JOUEURS = {Role.PRESIDENT, Role.VICE_PRESIDENT, Role.VICE_TROUDUC, Role.TROUDUC};
	private static final Role[] CINQ_JOUEURS = {Role.PRESIDENT, Role.VICE_PRESIDENT, Role.NEUTRE, Role.VICE_TROUDUC, Role.TROUDUC};
	private static final Role[] SIX_JOUEURS = {Role.PRESIDENT, Role.VICE_PRESIDENT, Role.NEUTRE, Role.NEUTRE, Role.VICE_TROUDUC, Role.TROUDUC};
	
	public static final Role[][] REPARTITION_ROLES = {DEUX_JOUEURS, TROIS_JOUEURS, QUATRE_JOUEURS, CINQ_JOUEURS, SIX_JOUEURS};
}
