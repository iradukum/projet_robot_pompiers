package strategie;

import donneesSimulation.DonneesSimulation;
import io.LecteurDonnees;
import scenario.*;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

/********************************************************************
 * Classe TestDebutant permettant de tester l'implémentation de la 
 * stratégie élémentaire
 ********************************************************************/

public class TestDebutant {
    public static void main(String[] args){
        try {
            DonneesSimulation data = LecteurDonnees.lire(args[0]);

            Simulateur simu = new Simulateur((long)0, data); 

            ChefPompierDebutant chef = new ChefPompierDebutant(simu);

            chef.assignTasks();

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}
