package scenario;

import robot.Robot;
import geographie.Carte;
import geographie.Carte.Direction;
import geographie.Case; 

/**********************************************************************
 * Classe EvenementDeplacement permet de déplacer le robot d'une case 
 **********************************************************************/

public class EvenementDeplacement extends Evenement {
    private Carte map; 
    private Direction sens;

    public EvenementDeplacement(long date, Robot robot, Direction sens, Carte map){
        super(date, robot);
        this.map = map; 
        this.sens = sens;
    }

    @Override 
    public void execute(){
        Case case_initiale = this.robot.getPosition();
        // Current position
        int ligne = case_initiale.getLigne(); 
        int colonne = case_initiale.getColonne();
        
        // Setting a new position for our robot 
        Case caseDest = case_initiale;

        switch(sens){
			case NORD:
                if (ligne !=0){
                    caseDest = new Case(ligne - 1, colonne);
                }
                break; 
			case SUD:
                if (ligne != map.getNbLignes()-1){
                    caseDest = new Case(ligne + 1, colonne);
                }
                break; 
			case OUEST:
                if (colonne != 0){
                    caseDest = new Case(ligne, colonne - 1);
                }
                break; 
			case EST:
                if (colonne != map.getNbColonnes()-1){
                    caseDest = new Case(ligne, colonne + 1);
                } 
                break;            
		}

        ligne = caseDest.getLigne();		
        colonne = caseDest.getColonne();
        this.robot.setPosition(map.getCase(ligne, colonne));     
    }
}