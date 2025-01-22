package robot;

import geographie.Case;
import geographie.Carte;
import geographie.Carte.Direction;
import geographie.Case.NatureTerrain;

/*****************************************************************************
 * Classe Drone implémente les méthodes propres au type de robot "Robot à roue"
 *****************************************************************************/

public class RobotARoue extends Robot{

	public RobotARoue(Case position, double vitesse, double remplissage) {
		super(position, vitesse, 5000, remplissage, 600, 100, 5, TypeRobot.ROBOTAROUE);
	}
	
	@Override
	public boolean zoneRemplissage(Case destination){
		Carte reference = destination.getCarteReference();
		for (Direction dir : Direction.values()){
			Case voisin = reference.getVoisin(destination, dir);
			if ((voisin != null) && (voisin.getNature() == NatureTerrain.EAU)){
				return true;
			}
		}
		return false;
	}

	@Override
	public double getVitesse(NatureTerrain terrain){
		return super.getVitesse();
	}
	
	@Override
	public boolean caseAutorisee(Case candidat){
		switch(candidat.getNature()){
			case HABITAT:
			case TERRAIN_LIBRE:
				return true;
			default:
				return false;
		}
	}

}
