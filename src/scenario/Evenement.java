package scenario;

import robot.Robot;

/**********************************************************************
 * Classe Evenement est la classe mère des classes des événements réels  
 **********************************************************************/

public abstract class Evenement {
    private long date;
    protected Robot robot;

    public Evenement(long date, Robot robot){
        this.date = date;
        this.robot = robot;
    }

    public long getDate(){
        return this.date; 
    }

    public Robot getRobot(){
        return this.robot;     
    }

    /* 
     * Permet d'exécuter l'événement en question 
     */
    public abstract void execute(); 
}

