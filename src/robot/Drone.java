package robot;

import geographie.Case;
import geographie.Case.NatureTerrain;

/**********************************************************************
 * Classe Drone implémente les méthodes propres au type de robot "Drone"
 **********************************************************************/

public class Drone extends Robot{

	public Drone(Case position, double vitesse, double remplissage) {
		super(position, vitesse, 10000, remplissage, 1800, 10000, 30,TypeRobot.DRONE);
	}
	
	@Override
	public boolean zoneRemplissage(Case destination){
		if (destination.getNature() == NatureTerrain.EAU){
			return true;
		}
		return false;
	}

	@Override 
	public double getVitesse(NatureTerrain terrain){
		return super.getVitesse();
	}
 	
	@Override
	public boolean caseAutorisee(Case candidat){
		return true;
	}
}
