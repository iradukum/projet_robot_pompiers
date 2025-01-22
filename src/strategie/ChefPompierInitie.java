package strategie;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;

import geographie.Incendie;
import geographie.Case;
import itineraires.Chemin;
import itineraires.Graphe;
import robot.Robot;
import scenario.Simulateur;
import scenario.EvenementRemplissage;
import scenario.EvenementIntervention;

/****************************************************************************************
 * Classe ChefPompierInitie implémentant une stratégie d'intervention un peu plus évoluée
 ****************************************************************************************/
public class ChefPompierInitie extends ChefPompier{
    private Queue<DateLiberation> dateFinActions = new PriorityQueue<DateLiberation>();
    private HashMap<Robot, Incendie> affectationIncendie; // Map associant un robot à l'incendie auquel il est affecté

    public ChefPompierInitie(Simulateur simu){
        super(simu);
        this.affectationIncendie = new HashMap<Robot, Incendie>();
        for (Robot bot : data.getListRobot()){
            affectationIncendie.put(bot, null);
        }
        dateFinActions.add(new DateLiberation(null, Long.MAX_VALUE));
    }

    @Override
    public void assignTasks(){
        Graphe graphe = new Graphe(data.getMap());
        Chemin chemin;
        double eauVerse;
        long date = this.simu.getDateSimulation(); 
        long dateEvent;
        ArrayList<Incendie> incendiesEteints = new ArrayList<Incendie>();
        while (!data.getListIncendie().isEmpty()){
            dateEvent = date;
            // On libère les robots qui terminent leur tâche au temps date.
            while (dateFinActions.peek().getDate() == date){
                dateFinActions.poll().getRobot().setOccupation(false);
            }

            // On parcourt les incendie encore en liste
            for (Incendie fire : data.getListIncendie()){
                // Si un incendie est totalement éteint, on le marque 
                // pour le retirer à la fin de l'itération
                if (fire.getQuantiteEau()==0){
                    incendiesEteints.add(fire);
                    for (Robot bot : affectationIncendie.keySet()){
                        if (affectationIncendie.get(bot) == fire){
                            affectationIncendie.replace(bot, null);
                        }
                    }
                    continue;
                }

                boolean feuAffecte = false;
                Robot candidat = null;
                Chemin shortestPath = null;
                for (Robot bot : data.getListRobot()){
                    // On choisit un robot libre
                    
                    if (!bot.getOccupation()){
                        // S'il est vide, on le fait remplir au max.
                        if (bot.getRemplissage() == 0){
                            affectationIncendie.replace(bot, null);
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
                            dateFinActions.add(new DateLiberation(bot, dateEvent));
                            continue;
                        }

                        
                        // Regarde si affecté à un incendie
                        if (affectationIncendie.get(bot) != null){
                            if (affectationIncendie.get(bot) == fire){
                                // On verse de l'eau sur l'incendie
                                dateEvent = date;
                                bot.setOccupation(true);
                                eauVerse = bot.getVolumeExtinction()* 30./bot.getTempsExtinction();
                                if (eauVerse>bot.getVolumeEauRestant()){
                                    eauVerse = bot.getVolumeEauRestant();
                                }
                                // creer et ajouter evenement intervention
                                dateEvent += (int) bot.tempsDeversementEau(eauVerse);
                                EvenementIntervention intervention = new EvenementIntervention(dateEvent, bot, eauVerse, fire);
                                fire.ajouterEau(eauVerse);
                                bot.deverserEau(eauVerse);
                                simu.ajouteEvenement(intervention);
                                dateFinActions.add(new DateLiberation(bot, dateEvent));
                                feuAffecte = true;                                
                                continue;
                            }
                        } 
                        // Si non affecté à un Incendie, calcul le chemin de bot à fire
                        else {
                            chemin = graphe.getTrajectory(bot, fire.getPosition());
                            if (!chemin.estVide()){
                                if (shortestPath == null){
                                    candidat = bot;
                                    shortestPath = chemin;
                                } else if (shortestPath.getTempsParcours() > chemin.getTempsParcours()){
                                    candidat = bot;
                                    shortestPath = chemin;
                                }
                            }
                        }
                    }
                }

                // regarde dans le cas où aucun robot n'a encore été affecté à cet incendie
                // prends le robot le plus proche
                if (!feuAffecte && shortestPath != null && !shortestPath.estVide()){
                    dateEvent = date;
                    affectationIncendie.replace(candidat, fire);
                    candidat.setOccupation(true);
                    shortestPath.effectueChemin(date, simu);
                    dateEvent += shortestPath.getTempsParcours();
                    dateFinActions.add(new DateLiberation(candidat, dateEvent));
                    candidat.setPosition(fire.getPosition());
                }
            }

            // Cas où des robots seraient libres
            for (Robot bot : data.getListRobot()){
                if (!bot.getOccupation()){
                    // On cherche l'incendie le plus proche
                    Incendie feuCandidat = null;
                    Chemin shortestPath = null;
                    for (Incendie fire : data.getListIncendie()){
                        chemin = graphe.getTrajectory(bot, fire.getPosition());
                        if (!chemin.estVide()){
                            if (shortestPath == null){
                                feuCandidat = fire;
                                shortestPath = chemin;
                            } else if (shortestPath.getTempsParcours() > chemin.getTempsParcours()){
                                feuCandidat = fire;
                                shortestPath = chemin;
                            }    
                        }
                    }
                    if (shortestPath != null && !shortestPath.estVide()){
                        affectationIncendie.replace(bot, feuCandidat);
                        bot.setOccupation(true);
                        shortestPath.effectueChemin(date, simu);
                        dateEvent += shortestPath.getTempsParcours();
                        dateFinActions.add(new DateLiberation(bot, dateEvent));
                        bot.setPosition(feuCandidat.getPosition());                       
                    }
                }
            }

            for (Incendie fire : incendiesEteints){
                data.eteindre(fire);
            }
            incendiesEteints = new ArrayList<Incendie>();

            date = dateFinActions.peek().getDate();
        }
        data.reset();
    }
}
