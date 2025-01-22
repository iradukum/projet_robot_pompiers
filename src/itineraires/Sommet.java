package itineraires;

import geographie.*;
import java.util.Comparator;

/*********************************************************************
 * Classe Sommet définie par la case associée et un temps de parcours
 *  depuis un sommet initial
 *********************************************************************/

class Sommet {
    private Case caseSommet;
    private double tempsParcours;

    public Sommet(Case CaseSommet, double dist){
        this.caseSommet = CaseSommet;
        this.tempsParcours = dist; // temps de parcours à partir d'un sommet source
    }

    public Case getCase(){
        return this.caseSommet;
    }

    public double getTempsParcours(){
        return this.tempsParcours;
    }

    public void setTempsParcours(long dist){
        this.tempsParcours = dist;
    }
}

class SommetComparator implements Comparator<Sommet>{

    public int compare(Sommet s1, Sommet s2){
        return Double.compare(s1.getTempsParcours(), s2.getTempsParcours());
    }
    
}
