package geographie;

import affichage.Affichage;

/************************************************************
 * Classe Incendie définie par sa position (une Case) 
 * et le nombre de litres d’eau nécessaires pour l’éteindre.
 ************************************************************/
public class Incendie {
    private Case position;
    private double quantiteEau;
    private double quantiteEauInit;
    private boolean etatAffecte = false; //true si un robot est affecte à cet incendie

    public Incendie(Case position, double quantiteEau){
        this.position = position;
        this.quantiteEau = quantiteEau;
        this.quantiteEauInit = quantiteEau;
    }

    public void associerCase(Carte map){
        this.position = map.getCase(this.getLigne(), this.getColonne());
    }

    public Case getPosition(){
        return this.position;
    }

    public int getLigne(){
        return this.position.getLigne();
    }

    public int getColonne(){
        return this.position.getColonne();
    }

    public double getQuantiteEau(){
        return this.quantiteEau;
    }

    public void resetQuantiteEau(){
        this.quantiteEau = this.quantiteEauInit;
    }

    /*
     * Permet de rajouter un volume d'eau sur l'incendie
     */
    public void ajouterEau(double volumeEau){
        if (this.quantiteEau <= volumeEau){
            this.quantiteEau = 0;
        } else {
            this.quantiteEau -= volumeEau;
        }
    }

    /*
     * Retourne true si l'incendie est affecté à un robot
     */
    public boolean getEtatAffecte(){
        return this.etatAffecte; 
    }

    public void setEtatAffecte(boolean etatAffecte){
        this.etatAffecte = etatAffecte; 
    }

    public void afficherIncendie(Affichage image){
        image.dessineIncendie(this.getColonne(), this.getLigne(), true);
    }

    @Override
    public String toString(){
        String s = "position = (" + position.getLigne() + "," + position.getColonne()+ ");\t intensite = " + quantiteEau;
        return s + "\n"; 
    }
}
