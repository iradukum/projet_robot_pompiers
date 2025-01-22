package affichage;

import java.awt.Color;
import gui.GUISimulator;
import gui.Simulable;
import gui.Text;
import gui.ImageElement;

import robot.Robot.TypeRobot;
import geographie.Case.NatureTerrain;

/*********************************************************
* Classe Affichage implémente les méthodes de dessin pour 
* afficher des éléments dans la fenêtre graphique      
**********************************************************/

public class Affichage{
    public GUISimulator gui;
    private int nbLignes;
    private int nbColonnes;
    private int taillecase;

    public Affichage(GUISimulator gui, int nbLignes, int nbColonnes, int taillecase){
        this.gui = gui; 
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.taillecase = taillecase;
    }

    /* 
     * Affiche la légende 
     */
    public void drawLegend(){
        this.dessineRobot(nbColonnes+1, 0, TypeRobot.DRONE, false);
        gui.addGraphicalElement(new Text((nbColonnes+4)*this.taillecase,(int)(0*this.taillecase+ this.taillecase*0.5), Color.BLACK, "DRONE"));
        this.dessineRobot(nbColonnes+1, 1, TypeRobot.ROBOTACHENILLE, false);
        gui.addGraphicalElement(new Text((nbColonnes+4)*this.taillecase,(int)(1*this.taillecase+ this.taillecase*0.5), Color.BLACK, "ROBOT A CHENILLE"));
        this.dessineRobot(nbColonnes+1, 2, TypeRobot.ROBOTAPATTES, false);
        gui.addGraphicalElement(new Text((nbColonnes+4)*this.taillecase,(int)(2*this.taillecase+ this.taillecase*0.5), Color.BLACK, "ROBOT A PATTES"));
        this.dessineRobot(nbColonnes+1, 3, TypeRobot.ROBOTAROUE, false);
        gui.addGraphicalElement(new Text((nbColonnes+4)*this.taillecase,(int)(3*this.taillecase+ this.taillecase*0.5), Color.BLACK, "ROBOT A ROUE"));
    }

    /* 
     * Dessine l'image contenu dans le fichier dont le nom est donné en paramètre 
     */
    public void dessineImage(int x, int y, int width, int height, String filename, boolean check){
        if (!check){
            gui.addGraphicalElement(new ImageElement(x*this.taillecase, y*this.taillecase, 
                                    filename, width, height, null));
        } else {
            if ((x>=this.nbColonnes) || (x<0) || (y<0) || (y>=this.nbLignes)){
                System.out.println("Invalid arguments x and y must verify \n\t 0 <= x < " + this.nbColonnes + "\t0 <= y < " + this.nbLignes);
                throw new IllegalArgumentException();
            }
            gui.addGraphicalElement(new ImageElement(x*this.taillecase, y*this.taillecase,
                                    filename, width, height, null));
        }
    }

    /* 
     * Dessine le robot dont le type est précisé en paramètre 
     */
    public void dessineRobot(int x, int y, TypeRobot type, boolean check){
        String path = "./images/";
        switch(type){
            case DRONE:
                this.dessineImage(x, y, this.taillecase, this.taillecase, path + "drone.png", check);
                break;
            case ROBOTACHENILLE:
                this.dessineImage(x, y, this.taillecase, this.taillecase, path + "robot_chenilles.png", check);
                break;
            case ROBOTAPATTES:
                this.dessineImage(x, y, this.taillecase, this.taillecase, path + "robot_patte.png", check);
                break;
            case ROBOTAROUE:
                this.dessineImage(x, y, this.taillecase, this.taillecase, path + "robot_roue.png", check);
                break;
            default:
                throw new IllegalArgumentException("Type Robot non reconnu");
        }
    }


    /* 
     * Dessine la case dont le type de terrain est précisé en paramètre 
     */
    public void dessineTerrain(int x, int y, NatureTerrain type, boolean check){
        String path = "./images/";
        switch(type){
            case EAU:
                this.dessineImage(x, y, this.taillecase, this.taillecase, path + "eau.png", check);
                break;
            case FORET:
                this.dessineImage(x, y, this.taillecase, this.taillecase, path + "foret.jpg", check);
                break;
            case HABITAT:
                this.dessineImage(x, y, this.taillecase, this.taillecase, path + "habitat.jpg", check);
                break;
            case ROCHE:
                this.dessineImage(x, y, this.taillecase, this.taillecase, path + "rocks.jpg", check);
                break;
            case TERRAIN_LIBRE:
                this.dessineImage(x, y, this.taillecase, this.taillecase, path + "terrain.jpg", check);
                break;
            default:
                throw new IllegalArgumentException("Type Terrain non reconnu");
        }
    }


    /* 
     * Dessine l'incendie 
     */
    public void dessineIncendie(int x, int y, boolean check){
        this.dessineImage(x, y, (int)(this.taillecase), (int)(this.taillecase), "./images/incendie.png", check);
    }
}
