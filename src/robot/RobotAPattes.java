package robot;

import geographie.Case;
import geographie.Case.NatureTerrain;

/*******************************************************************************
 * Classe Drone implémente les méthodes propres au type de robot "Robot à pattes"
 *******************************************************************************/

public class RobotAPattes extends Robot{

	public RobotAPattes(Case position) {
		super(position, 30, 10000000, Double.MAX_VALUE, 0, 10, 1,TypeRobot.ROBOTAPATTES);
	}

	@Override
	public double getVitesse(){
		if (super.getPosition().getNature() == NatureTerrain.ROCHE){
			return 10;
		}
		return super.getVitesse();
	}

	@Override 
	public double getVitesse(NatureTerrain terrain){
		if (terrain == NatureTerrain.ROCHE){
			return 10;
		}
		return super.getVitesse();
	}

	@Override
	public boolean zoneRemplissage(Case destination){
		return true;
	}

	@Override
	public double tempsRemplir(double objectif){
		return 0;
	}

	@Override
	public void deverserEau(double vol){
		return;
	}

	@Override
	public boolean caseAutorisee(Case candidat){
		switch(candidat.getNature()){
			case EAU:
				return false;
			default:
				return true;
		}
	}
}
