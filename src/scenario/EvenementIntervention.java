package scenario;

import geographie.*;
import robot.Robot;

/*******************************************************************
 * Classe EvenementInterventionMesuree décrit le déversement d'une
 * quantité d'eau fixé par un robot sur un incendie
 *******************************************************************/

public class EvenementIntervention extends Evenement {
    private Incendie incendie;
    private double volumeEauDeverse;

    public EvenementIntervention(long date, Robot robot, double volumeEauDeverse, Incendie incendie) {
        super(date, robot);
        this.incendie = incendie;
        this.volumeEauDeverse = volumeEauDeverse;
    }

    @Override
    public void execute(){
        boolean positionCorrecte;
        positionCorrecte = (this.robot.getLigne() == this.incendie.getLigne())
                        && (this.robot.getColonne() == this.incendie.getColonne());
        try{
            if(!positionCorrecte){
                throw new IllegalArgumentException("Le robot n'est pas sur la case de l'incendie");
            }
            this.robot.deverserEau(volumeEauDeverse);
            this.incendie.ajouterEau(volumeEauDeverse);

            if (incendie.getQuantiteEau() == 0){
                System.out.print("\n\n==================ET UN INCENDIE EN MOINS, BRAVO LES ROBOTS==================\n\n");
            }
            
        }catch(IllegalArgumentException e){
            System.out.println(e);
        }
    }
}
