package strategie;

import java.util.TreeMap;
import java.util.ArrayList;

import scenario.EvenementIntervention;
import scenario.EvenementRemplissage;
import scenario.Simulateur;
import geographie.*;
import robot.Robot;
import itineraires.*;

/********************************************************************************
 * Classe ChefPompierDebutant implémentant une stratégie élémentaire consistant 
 * à envoyer des robots intervenir sur des incendies sans recherche d'optimisation
 ********************************************************************************/

public class ChefPompierDebutant extends ChefPompier{
    private TreeMap<Long, ArrayList<Incendie>> incendiesNonAffectes;  //Map associant les incendies à leurs dates de désaffectation 
    private TreeMap<Long, ArrayList<Robot>> robotsDisponibles; //Map associant les robots à leurs dates de libération 

    public ChefPompierDebutant(Simulateur simu){
        super(simu);
        this.incendiesNonAffectes = new TreeMap<Long, ArrayList<Incendie>>();
        incendiesNonAffectes.put(Long.MAX_VALUE, null);
        this.robotsDisponibles = new TreeMap<Long, ArrayList<Robot>>();
        robotsDisponibles.put(Long.MAX_VALUE, null);
    }

    @Override
    public void assignTasks(){
        /* Initialisation */
        Graphe graphe = new Graphe(data.getMap());
        Chemin chemin;
        double eauVerse;
        long date = this.simu.getDateSimulation(); 
        long dateEvent;
        ArrayList<Incendie> incendiesEteints = new ArrayList<Incendie>();
        while (!data.getListIncendie().isEmpty()){
            for (Incendie fire : incendiesEteints){
                data.eteindre(fire);
            }
            if (data.getListIncendie().size()==0){
                // il ne reste aucun incendie 
                break;
            }
            if (incendiesNonAffectes.containsKey(date)){
                for (Incendie fire : incendiesNonAffectes.get(date)){
                    // On désaffecte l'incendie puisqu'il n'est plus affecté à un robot à cette date 
                    fire.setEtatAffecte(false);
                }
                incendiesNonAffectes.remove(date); // On supprime les incendies qu'on vient de les désafecter  
            }
            if (robotsDisponibles.containsKey(date)){
                for (Robot bot : robotsDisponibles.get(date)){
                    // On libère le bot puisqu'il a terminé sa mission à cette date 
                    bot.setOccupation(false);
                }
                robotsDisponibles.remove(date);
            }
            incendiesEteints = new ArrayList<Incendie>();
            for (Incendie fire : data.getListIncendie()){
                if (fire.getQuantiteEau()==0.0){
                    incendiesEteints.add(fire);
                    continue;
                }
                // Ici, On traitera le cas où le feu n'est ni éteint ni affecté
                if (!fire.getEtatAffecte()){
                    for (Robot bot : data.getListRobot()){
                        if (!bot.getOccupation()){  // si le robot est dispo
                            if (bot.getRemplissage() == 0){ // si le robot n'a plus d'eau 
                                dateEvent = date;
                                bot.setOccupation(true);
                                if (!bot.zoneRemplissage(bot.getPosition())){
                                    chemin = bot.caseRemplissage(graphe);
                                    Case destination = chemin.getCaseDestination();
                                    chemin.effectueChemin(date, this.simu);
                                    dateEvent += chemin.getTempsParcours();
                                    bot.setPosition(destination);    
                                }
                                dateEvent += (long) bot.tempsRemplir(1.);
                                bot.setRemplissage(1.);
                                EvenementRemplissage glouglou = new EvenementRemplissage(dateEvent, bot);
                                simu.ajouteEvenement(glouglou);
                                if (!robotsDisponibles.containsKey(dateEvent)){
                                    robotsDisponibles.put(dateEvent, new ArrayList<Robot>());
                                }
                                robotsDisponibles.get(dateEvent).add(bot); // Le bot sera libéré à dateEvent 
                                continue;
                            }
                            // si le robot contient encore de l'eau 
                            chemin = graphe.getTrajectory(bot, fire.getPosition()); 
                            if (!(chemin.getCases().isEmpty())){
                                bot.setOccupation(true);
                                chemin.effectueChemin(date, this.simu);
                                dateEvent = date + chemin.getTempsParcours();
                                fire.setEtatAffecte(true);
                                bot.setPosition(fire.getPosition());
                                if (fire.getQuantiteEau()>bot.getVolumeEauRestant()){
                                    eauVerse = bot.getVolumeEauRestant();
                                } else {
                                    eauVerse = fire.getQuantiteEau();
                                }
                                dateEvent += (int) bot.tempsDeversementEau(eauVerse);
                                EvenementIntervention intervention = new EvenementIntervention(dateEvent, bot, eauVerse, fire);
                                fire.ajouterEau(eauVerse);
                                bot.deverserEau(eauVerse);
                                simu.ajouteEvenement(intervention);
                                if (!incendiesNonAffectes.containsKey(dateEvent)){
                                    incendiesNonAffectes.put(dateEvent,new ArrayList<Incendie>());
                                } 
                                if (!robotsDisponibles.containsKey(dateEvent)){
                                    robotsDisponibles.put(dateEvent, new ArrayList<Robot>());
                                }
                                // fire ne sera plus affecté à cette date 
                                incendiesNonAffectes.get(dateEvent).add(fire);
                                // bot sera libéré à cette date 
                                robotsDisponibles.get(dateEvent).add(bot);
                                break;
                            }
                        }
                    }
                }
            }
        
            if (incendiesNonAffectes.firstKey() < robotsDisponibles.firstKey()){
                date = incendiesNonAffectes.firstKey();
            } else {
                date = robotsDisponibles.firstKey();
            }
        }
        data.reset();
    }
}