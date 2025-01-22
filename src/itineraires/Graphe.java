package itineraires;

import java.util.*;

import geographie.*;
import geographie.Carte.Direction;
import robot.*;

/****************************************************************
 * Classe Graphe contenant les méthodes nécessaires au calcul du 
 * plus court chemin en utilisant l'algorithme de Dijkstra
 ****************************************************************/

public class Graphe {
    public Carte carte;

    public Graphe(Carte carte){
        this.carte = carte;
    }

    /*
     * Effectue l'initialisation de l'algorithme de Dijkstra, en associant à chaque
     * case un temps de parcours initialisé à l'infini
     */
    private Map<Case, Double> initDijkstra(){
        Map<Case, Double> tempsParcours = new HashMap<Case, Double>();
        for (int i = 0; i < this.carte.getNbLignes(); i++){
            for (int j = 0; j < this.carte.getNbColonnes(); j++){
                tempsParcours.put(this.carte.getCase(i, j), Double.MAX_VALUE);
            }
        }
        return tempsParcours;
    }

    /*
     * Renvoie le plus court chemin qu'un robot doit emprunter pour se rendre à une case donnée
     */
    public Chemin getTrajectory(Robot robot, Case caseDest){
        Chemin chemin = new Chemin(this.carte, robot);

        try{
            if (!(0 <= caseDest.getLigne() && caseDest.getLigne() < this.carte.getNbLignes()
            && 0 <= caseDest.getColonne() && caseDest.getColonne() < this.carte.getNbColonnes())){
                throw new IllegalArgumentException("La destination est invalide");
            }

            // Permet de conserver les cases empruntées au cours de l'exécution de l'algorithme
            HashMap<Case, Case> casesPrec = new HashMap<Case, Case>();

            // sommets contient l'ensemble des sommets non marqués mais à coût fini
            Comparator<Sommet> comparator = new SommetComparator();
            Queue<Sommet> sommets = new PriorityQueue<Sommet>(comparator);
            
            Case caseSrc = robot.getPosition();
    
            // Initialisation de l'algorithme
            Map<Case, Double> tempsParcours = this.initDijkstra();
            tempsParcours.put(caseSrc, 0.0);
            sommets.add(new Sommet(caseSrc, 0.0));
    
            while (!sommets.isEmpty()){
                Sommet sommet = sommets.poll();
                for(Direction dir : Direction.values()){
                    Case voisin = carte.getVoisin(sommet.getCase(), dir);
                    
                    boolean caseInvalide = casesPrec.containsKey(voisin) || (voisin == null) || (!(robot.caseAutorisee(voisin)));
                    if (caseInvalide){
                        continue;
                    }
                    
                    double poids = robot.tempsParcoursCase(sommet.getCase().getNature());
                    if (tempsParcours.get(sommet.getCase()) + poids < tempsParcours.get(voisin)){
                        tempsParcours.put(voisin, tempsParcours.get(sommet.getCase()) + poids);
                        sommets.add(new Sommet(voisin, tempsParcours.get(voisin)));
                        casesPrec.put(voisin, sommet.getCase());
                    }  
                }
                if (casesPrec.containsKey(caseDest)){
                    break;
                }
    
            }
            
            // On retrouve le chemin à parcourir en partant de la case destination
            int ligneDest = caseDest.getLigne();
            int colDest = caseDest.getColonne();
            if (tempsParcours.get(this.carte.getCase(ligneDest, colDest)) != Double.MAX_VALUE){
                Case caseCourante = this.carte.getCase(ligneDest, colDest);
                while(!(caseCourante.equals(caseSrc))){
                    chemin.ajouteCase(caseCourante);
                    caseCourante = casesPrec.get(caseCourante);
                }
                chemin.ajouteCase(caseSrc);
            }
            
        } catch (IllegalArgumentException e){
            System.out.println(e);
        }

        return chemin;
    }

}
