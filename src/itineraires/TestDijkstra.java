package itineraires;

import geographie.*;
import robot.*;
import geographie.Case.NatureTerrain;
import scenario.*;
import donneesSimulation.*;

/********************************************************************
 * Classe TestDijkstra permettant de tester l'algorithme de calcul 
 * du plus court chemin
 ********************************************************************/

public class TestDijkstra {
    public static void main(String[] args){
        // Création d'une carte de test
        Carte carte = new Carte(8, 8, 10000);
        for (int i=0; i<8;i++){
            if(i%3 == 0){
                carte.setNatureCase(i, i, NatureTerrain.EAU);
            }
            else{
                carte.setNatureCase(i, i, NatureTerrain.FORET);
            }
            carte.setNatureCase(i, (i+1)%8, NatureTerrain.FORET);
            if(i%2 == 0){
                carte.setNatureCase(i, (i+2)%8, NatureTerrain.ROCHE);
                carte.setNatureCase(i, (i+7)%8, NatureTerrain.ROCHE);
            }
            else{
                carte.setNatureCase(i, (i+2)%8, NatureTerrain.TERRAIN_LIBRE);
                carte.setNatureCase(i, (i+7)%8, NatureTerrain.TERRAIN_LIBRE);
            }
            carte.setNatureCase(i, (i+3)%8, NatureTerrain.HABITAT);
            carte.setNatureCase(i, (i+4)%8, NatureTerrain.TERRAIN_LIBRE);
            carte.setNatureCase(i, (i+5)%8, NatureTerrain.EAU);
            carte.setNatureCase(i, (i+6)%8, NatureTerrain.FORET);
        }

        // Création des robots
        Robot roue = new RobotARoue(carte.getCase(0, 3), (double)80, (float)30); 
        Robot chenille = new RobotAChenille(carte.getCase(4, 2), 60, 30);
        Robot drone1 = new Drone(carte.getCase(3, 3), 100, 30);
        Robot drone2 = new Drone(carte.getCase(6, 7), 50, 30);
        DonneesSimulation data = new DonneesSimulation(); 
        data.associerCarte(carte); 
        data.ajouterRobot(chenille);
        data.ajouterRobot(roue);
        data.ajouterRobot(drone1);
        data.ajouterRobot(drone2);

        Simulateur simu = new Simulateur((long)10, data); 
        Graphe graphe = new Graphe(carte);
        
        Case caseFinal1 = new Case(3, 4);
        Case caseFinal2 = new Case(2, 1);
        Case caseFinal3 = new Case(7, 0);
        Case caseFinal4 = new Case(0, 7);
        Chemin chemin1 = graphe.getTrajectory(chenille, caseFinal1);
        Chemin chemin2 = graphe.getTrajectory(roue, caseFinal2);
        Chemin chemin3 = graphe.getTrajectory(drone1, caseFinal3);
        Chemin chemin4 = graphe.getTrajectory(drone2, caseFinal4);
        chemin1.effectueChemin((long)10, simu);
        chemin2.effectueChemin((long)13, simu);
        chemin3.effectueChemin((long)16, simu);
        chemin4.effectueChemin((long)16, simu);
    }
}
