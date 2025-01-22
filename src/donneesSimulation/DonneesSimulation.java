package donneesSimulation;

import java.util.ArrayList;

import geographie.Carte;
import geographie.Incendie;
import robot.Robot;

/*********************************************************
 * Classe DonneesSimulation englobe tous 
 * les éléments de la simulation
 *********************************************************/

public class DonneesSimulation{
    private Carte map;
    private ArrayList<Robot> listRobot;
    private ArrayList<Incendie> listIncendie;
    private ArrayList<Incendie> listIncendieEteint;

    public DonneesSimulation(){
        this.listRobot = new ArrayList<Robot>();
        this.listIncendie = new ArrayList<Incendie>();
        this.listIncendieEteint = new ArrayList<Incendie>();
    }

    public Carte getMap(){
        return this.map;
    }

    public ArrayList<Robot> getListRobot(){
        return this.listRobot; 
    }
       
    public ArrayList<Incendie> getListIncendie(){
        return this.listIncendie; 
    }

    public ArrayList<Incendie> getListIncendieEteint(){
        return this.listIncendieEteint; 
    }

    public void associerCarte(Carte map){
        this.map = map;
    }

    public void ajouterRobot(Robot robot){
        this.listRobot.add(robot);
    }

    public boolean contientRobot(Robot robot){
        return this.listRobot.contains(robot);
    }

    /* 
    * Permet de remettre les robots à leurs positions initiales
    */
    public void resetRobots(){
        for (Robot bot : this.listRobot){
            bot.reset();
        }
    }

    public void ajouterIncendie(Incendie fire){
        this.listIncendie.add(fire);
    }

    /* 
    * Permet de réinitialiser la liste des incendies 
    */
    public void resetIncendies(){
        for (Incendie fire : this.listIncendieEteint){
            listIncendie.add(fire);
        }
        listIncendieEteint.clear();
        for (Incendie fire : this.listIncendie){
            fire.resetQuantiteEau();
        }
    }

    public void eteindre(Incendie fire){
        this.listIncendieEteint.add(fire);
        this.listIncendie.remove(fire);
    }

    /*
     * Permet de réinitialiser les données de la simulation 
     */
    public void reset(){
        this.resetIncendies();
        this.resetRobots();
    }
}