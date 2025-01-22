package itineraires;

import java.util.Iterator;
import java.util.LinkedList;

import geographie.*;
import geographie.Carte.Direction;
import robot.*;
import scenario.*;

/********************************************************************
 * Classe Chemin caracterisé par une séquence de cases, le robot devant
 * exécuter ce chemin et le temps de parcours correspondant
 ********************************************************************/

public class Chemin {
    private LinkedList<Case> tabCases;
    private Robot robot;
    private double tempsParcours;

    public Chemin(Carte graphe, Robot robot){
        this.tabCases = new LinkedList<Case>();
        this.tempsParcours = 0.0;
        this.robot = robot;
    }

    public LinkedList<Case> getCases(){
        return this.tabCases;
    }

    public long getTempsParcours(){
        return (long) this.tempsParcours;
    }
    
    /* 
     * Ajoute une case en tête de la table des cases constituant le chemin
     */
    public void ajouteCase(Case newCase){
        this.tabCases.addFirst(newCase);
        this.tempsParcours += this.robot.tempsParcoursCase(newCase.getNature());
    }

    public Case getCaseDestination(){
        return this.tabCases.getLast();
    }

    /*
     * Renvoie true si le chemin ne contient aucune case
     */
    public boolean estVide(){
        return tabCases.isEmpty();
    }

    /*
     * Permet de créer la séquence d'évenements nécessaires pour effectuer un chemin
     */
    public void effectueChemin(long dateDebut, Simulateur simu){
        Iterator<Case> caseIterator = this.tabCases.iterator();
        Direction dir;
        long date = dateDebut;
        if (caseIterator.hasNext()){
            Case caseCourante = caseIterator.next();
            while(caseIterator.hasNext()){
                Case caseDest = caseIterator.next();
                if(caseDest.getLigne() == caseCourante.getLigne()-1){ dir = Direction.NORD;}
                else if(caseDest.getLigne() == caseCourante.getLigne()+1){ dir = Direction.SUD;}
                else if(caseDest.getColonne() == caseCourante.getColonne()-1){ dir = Direction.OUEST;}
                else if(caseDest.getColonne() == caseCourante.getColonne()+1){ dir = Direction.EST;}
                else{
                    throw new IllegalArgumentException("Le chemin n'est pas correct");
                }
                date += (int)this.robot.tempsParcoursCase(caseCourante.getNature());
                EvenementDeplacement deplacement = new EvenementDeplacement(date, this.robot, dir, this.robot.getPosition().getCarteReference());
                simu.ajouteEvenement(deplacement);
                caseCourante = caseDest;
            }
        }
    }

    @Override
    public String toString(){
        String s = "Chemin : ";
        for (Case caseParcouru:tabCases){
            s += "(" + caseParcouru.getLigne() + " , " + caseParcouru.getColonne() + ")";
        }
        return s;
    }

}
