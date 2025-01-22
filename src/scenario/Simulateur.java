package scenario;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import affichage.Affichage;
import donneesSimulation.DonneesSimulation;
import gui.GUISimulator;
import gui.Simulable;
import robot.Robot;
import geographie.*;

/************************************************************
 * Classe Simulateur traite les évènements de manière adéquate 
 * en fonction des données et de l’état de la simulation
 **********************************************************/

public class Simulateur implements Simulable{ 
    private long dateSimulation; 
    private long dateInitiale;  
    private long dateFinale = 0; 
    private long duree = 1; 
    private DonneesSimulation data; 
    private Affichage affichage;
    private HashMap<Long,ArrayList<Evenement>> eventsCatalog; 
    private int nbEvents = 0;
    private int nbEventsCompleted = 0; 
    private boolean simulationDejaTerminee = false;


    public Simulateur(long dateSimulation, DonneesSimulation data){
        this.eventsCatalog = new HashMap<Long, ArrayList<Evenement>>(); 
        this.dateSimulation = dateSimulation; 
        this.dateInitiale = dateSimulation; 
        this.data = data; 
        int nbLignes = this.data.getMap().getNbLignes(); 
        int nbColonnes = this.data.getMap().getNbColonnes(); 
        int tailleCases = 900/nbColonnes; 
         // On crée la fenêtre graphique dans laquelle les dessins seront effectués 
        GUISimulator gui = new GUISimulator(nbColonnes*tailleCases + 10*tailleCases, 
                                            nbLignes*tailleCases, Color.decode("#D3D3D3"));
        gui.setSimulable(this);	
        this.affichage = new Affichage(gui, nbLignes, nbColonnes, tailleCases);
        afficheScene();
    }

    public long getDateSimulation(){
        return this.dateSimulation; 
    }

    public DonneesSimulation getData(){
        return this.data; 
    }

    /*
     * Permet d'ajouter l'événement entré en paramètre au gestionnaire d'événements 
     */
    public void ajouteEvenement(Evenement e){
        long date = e.getDate(); 
        if (eventsCatalog.containsKey(date)){
            eventsCatalog.get(date).add(e); 
        }
        else{
            ArrayList<Evenement> newEventList = new ArrayList<Evenement>();
            newEventList.add(e); 
            this.eventsCatalog.put(date, newEventList);
        }    
        // On incrémente le nombre d'événements 
        this.nbEvents +=1; 
        if (date > this.dateFinale){
            this.dateFinale = date; 
        }
    } 

    // A faire une fois que tous les évènements ont été créés pour 
    // Calculer l'incrément de temps
    public void incrementeDate(){
        if (nbEvents == 0){
            throw new IllegalArgumentException("Pas d'évènement dans la simu!");
        }
        duree = (this.dateFinale - this.dateInitiale)/nbEvents; 
        if (duree == 0){
            duree = 1; 
        }
        this.dateSimulation += duree; 
    }

    /*
     * Permet de réaliser les événements non effectués dont la date 
     * est inférieure à celle de la simulation 
     */
    public void trigger(){
        // On définit d'abord la date de départ d'exécution
        long dateDebut; 
        if (dateSimulation == dateInitiale){
            dateDebut = dateSimulation; 
        } else {
            dateDebut = dateSimulation - duree + 1;
        }
        
        for (long date = dateDebut; date <=dateSimulation; date++){
            if (eventsCatalog.containsKey(date)){
                for (Evenement e: eventsCatalog.get(date)){
                    e.execute(); 
                    nbEventsCompleted += 1; 
                }
            }
        }
    }

    @Override
    public void next(){
        if (!simulationTerminee()){
            System.out.print("[next..]");
            trigger(); 
            incrementeDate(); 
        } else if (simulationDejaTerminee == false){
            System.out.print("\n=========LES ROBOTS SONT EFFICACES, TOUS LES INCENDIES ONT ETE ETEINTS=========\n");
            System.out.print("\n=================VEUILLEZ QUITTER OU RECOMMENCER LA SIMULATION=================\n\n");
            simulationDejaTerminee = true;
        }
        afficheScene();
    }

    @Override 
    public void restart(){
        System.out.print("\n\n====================REPRENONS LA SIMULATION A PARTIR DU DEBUT====================\n\n");
        this.dateSimulation = dateInitiale; 
        this.nbEventsCompleted = 0;
        // On remet les données de la simulation à leur état initial
        this.data.reset(); 
        afficheScene();
    }

    public boolean simulationTerminee(){
        if (nbEvents == nbEventsCompleted){
            return true;
        }
        return false; 
    }

    /*
     * Réalise l'affichage des données de la simulation 
     */
    public void afficheScene(){
        affichage.gui.reset();    // clean 
        affichage.drawLegend();
        this.data.getMap().afficherCarte(affichage);
        for (Incendie fire : this.data.getListIncendie()){
            if (fire.getQuantiteEau() > 0){
                fire.afficherIncendie(affichage);
            }
        }
        for (Robot robot : this.data.getListRobot()){
            robot.afficherRobot(affichage);
        }       
    }   
}
