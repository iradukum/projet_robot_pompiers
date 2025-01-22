package scenario;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import io.*;
import donneesSimulation.DonneesSimulation;
import robot.*; 
import geographie.*;
import geographie.Carte.Direction;

/********************************************************************
 * Classe TestEvenement permettant de tester l'implémentation des
 * événements intervenant dans la simulation
 ********************************************************************/

public class TestEvenement {
    public static void main(String[] args) {
        try {
            DonneesSimulation data = LecteurDonnees.lire("cartes/carteSujet.map");

            Simulateur simu = new Simulateur((long)0, data); 

            // On teste le déplacement du premier robot de 4 cases direction nord
            Robot robot1 = data.getListRobot().get(0);

            long date = simu.getDateSimulation();
            date += robot1.tempsParcoursCase(robot1.getPosition().getNature());
            simu.ajouteEvenement(new EvenementDeplacement((long)date, robot1, Direction.NORD, data.getMap()));
            date += robot1.tempsParcoursCase(robot1.getPosition().getNature());
            simu.ajouteEvenement(new EvenementDeplacement((long)date, robot1, Direction.NORD, data.getMap()));
            date += robot1.tempsParcoursCase(robot1.getPosition().getNature());
            simu.ajouteEvenement(new EvenementDeplacement((long)date, robot1, Direction.NORD, data.getMap()));
            date += robot1.tempsParcoursCase(robot1.getPosition().getNature());
            simu.ajouteEvenement(new EvenementDeplacement((long)date, robot1, Direction.NORD, data.getMap()));

            // On teste le remplissage et l'intervention
            Robot robot2 = data.getListRobot().get(1);
            Incendie incendie1 = data.getListIncendie().get(4);
            date += robot2.tempsParcoursCase(robot2.getPosition().getNature());
            simu.ajouteEvenement(new EvenementDeplacement((long)date, robot2, Direction.NORD, data.getMap()));
            date += robot2.tempsDeversementEau(robot2.getReservoir());
            simu.ajouteEvenement(new EvenementIntervention((long)date, robot2, robot2.getReservoir(), incendie1));
            date += robot2.tempsParcoursCase(robot2.getPosition().getNature());
            simu.ajouteEvenement(new EvenementDeplacement((long)date, robot2, Direction.OUEST, data.getMap()));
            date += robot2.tempsParcoursCase(robot2.getPosition().getNature());
            simu.ajouteEvenement(new EvenementDeplacement((long)date, robot2, Direction.OUEST, data.getMap()));
            date += robot2.getTempsRemplissage();
            simu.ajouteEvenement(new EvenementRemplissage((long)date, robot2));
            date += robot2.tempsParcoursCase(robot2.getPosition().getNature());
            simu.ajouteEvenement(new EvenementDeplacement((long)date, robot2, Direction.EST, data.getMap()));
            date += robot2.tempsParcoursCase(robot2.getPosition().getNature());
            simu.ajouteEvenement(new EvenementDeplacement((long)date, robot2, Direction.EST, data.getMap()));
            date += robot2.tempsDeversementEau(incendie1.getQuantiteEau());
            simu.ajouteEvenement(new EvenementIntervention((long)date, robot2, incendie1.getQuantiteEau(), incendie1));

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + "cartes/carteSujet.map" + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + "cartes/carteSujet.map" + " invalide: " + e.getMessage());
        }
    }
}
