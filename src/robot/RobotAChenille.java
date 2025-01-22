package robot;

import geographie.Carte;
import geographie.Case;
import geographie.Carte.Direction;
import geographie.Case.NatureTerrain;

/************************************************************************************
 * Classe Drone implémente les méthodes propres au type de robot "Robot à chenilles"
 ************************************************************************************/

public class RobotAChenille extends Robot{

	public RobotAChenille(Case position, double vitesse, double remplissage) {
		super(position, vitesse, 2000, remplissage, 300, 100, 8, TypeRobot.ROBOTACHENILLE);
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
	public double getVitesse(){
		if (super.getPosition().getNature() == NatureTerrain.FORET){
			return super.getVitesse()/2;
		}
		return super.getVitesse();
	}

	@Override
	public double getVitesse(NatureTerrain terrain){
		if (terrain== NatureTerrain.FORET){
			return super.getVitesse()/2;
		}
		return super.getVitesse();
	}

	@Override
	public boolean caseAutorisee(Case candidat){
		switch(candidat.getNature()){
			case ROCHE:
			case EAU:
				return false;
			default:
				return true;
		}
	}
}
