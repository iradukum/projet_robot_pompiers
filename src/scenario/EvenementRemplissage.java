package scenario;

import robot.*;

/************************************************************
 * Classe EvenementRemplissage correspondant au remplissage 
 * complet du réservoir du robot associé à l'évenement
 ************************************************************/

 public class EvenementRemplissage extends Evenement{

    public EvenementRemplissage(long date, Robot robot){
        super(date, robot); 
    }

    @Override
    public void execute(){
        this.robot.setRemplissage(1.0);
    }
}
