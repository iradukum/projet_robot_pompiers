package robot;


import affichage.Affichage;
import geographie.Case;
import geographie.Case.NatureTerrain;
import itineraires.Chemin;
import itineraires.Graphe;

/*********************************************************
* Classe Robot spécifie les propriétés et opérations 
* communes à tous les robots
**********************************************************/

public abstract class Robot{
	private Case position; // position actuelle
	private Case positionInit; // pour la réinitialisation
	private double vitesse; // vitesse en m/s
	private int reservoir; // volume maximal d'eau en litres 
	private double remplissage; // remplissage en pourcentage 
	private double tempsRemplissage; // temps en seconde pour se remplir entièrement remplissage 0->100%
	private double volumeExtinction; // volume d'eau déversé par période de temps (temps Extinction) 
	private double tempsExtinction; // temps necessaire pour déverser volumeExtinction
	private TypeRobot type;
	private boolean estOccupe;

	public enum TypeRobot{DRONE, ROBOTACHENILLE, ROBOTAPATTES, ROBOTAROUE};

	public Robot(Case position, double vitesse, int reservoir, double remplissage,double tempsRemplissage, 
				double volumeExtinction, double tempsExtinction, TypeRobot type) {
		this.positionInit = position;
		this.position = position;
		this.vitesse = vitesse;
		this.reservoir = reservoir;
		this.remplissage = remplissage;
		this.tempsRemplissage = tempsRemplissage;
		this.volumeExtinction = volumeExtinction;
		this.tempsExtinction = tempsExtinction;
		this.type = type;
		this.estOccupe = false;
		if (!caseAutorisee(position)){
			throw new IllegalArgumentException("Le Robot ne peut pas être sur cette case!");
		}
	}

	/*
	 * Retourne le type du robot 
	 */
	public TypeRobot getTypeRobot() {
		return this.type;
	}

	/*
	 * Retourne la position du robot 
	 */
	public Case getPosition() {
		return this.position;
	}

	/*
	 * Modifie la position du robot 
	 */
	public void setPosition(Case destination) {
		this.position = destination;
	}

	/*
	 * Réinitialise la position robot à sa position initiale 
	 */ 
	public void resetPosition(){
		this.position = this.positionInit;
	}

	public int getLigne(){
		return this.position.getLigne();
	}

	public int getColonne(){
		return this.position.getColonne();
	}

	/*
	 * Retourne si le robot peut se placer sur la case entrée en paramètre
	 */
	public abstract boolean caseAutorisee(Case candidat);

	/*
	 * Permet de savoir si le robot est occupé 
	 */
	public boolean getOccupation(){
		return this.estOccupe;
	}

	/*
	 * Permet de modifier l'état du robot en le rendant 
	 * occupé ou pas selon le booléen entré en paramètre 
	 */
	public void setOccupation(boolean occupation){
		this.estOccupe = occupation;
	}
	
	/*
	 * Retourne la vitesse du robot
	 */
	public double getVitesse() {
		return this.vitesse;
	}

	/*
	 * Retourne le temps de parcours d'une case en fonction du terrain proposé en paramètre 
	 */
	public double tempsParcoursCase(NatureTerrain nature){
		return (double)(this.position.getCarteReference().getTailleCases())/(this.getVitesse(nature)/3.6);
	}

	/*
	 * Retourne la vitesse du robot en fonction 
	 * de la nature du terrain proposée en paramète 
	 */
	public abstract double getVitesse(NatureTerrain terrain);

	/*
	 * Retourne le volume d'eau maximale que peut contenir un robot 
	 */
	public int getReservoir() {
		return this.reservoir;
	}

	/*
	 * Retourne le niveau de remplissage en pourcentage 
	 */
	public double getRemplissage() {
		return this.remplissage;
	}

	/*
	 * Modifie le niveau de remplissage 
	 */
	public void setRemplissage(double remplissage) {
		this.remplissage = remplissage;
	}
	
	/*
	 * Calcule le temps nécessaire pour remplir 
	 * le réservoir à un pourcentage donné en paramètre
	 */
	public double tempsRemplir(double objectif){
		if (objectif <= remplissage){
			return 0;
		}
		return (objectif - remplissage) * tempsRemplissage;
	}

	/*
	 * Vérifie si le robot se trouve sur une case lui permettant de se remplir 
	 */
	public abstract boolean zoneRemplissage(Case destination);

	public double getTempsRemplissage() {
		return this.tempsRemplissage;
	}

	/*
	 * Retourne le chemin pour se rendre à la case de remplissage la plus proche 
	 */
	public Chemin caseRemplissage(Graphe graphe){
		Chemin chemin = null;
		Chemin newChemin;
		Case newCase;
		long tempsParcours = Long.MAX_VALUE;
		for (int i = 0; i < graphe.carte.getNbLignes(); i++){
			for (int j = 0; j < graphe.carte.getNbColonnes(); j++){
				newCase = graphe.carte.getCase(i, j);
				if (this.zoneRemplissage(newCase)){
					newChemin = graphe.getTrajectory(this, newCase);
					long newTemps = newChemin.getTempsParcours();
					if (!newChemin.estVide() && (newTemps < tempsParcours)){
						chemin = newChemin;
						tempsParcours = newTemps;
					}
				}
			}
		}
		return chemin;
	}

	/*
	 * Retourne le volume d'eau déversé par période de temps 
	 */
	public double getVolumeExtinction() {
		return this.volumeExtinction;
	}

	/*
	 *  Retourne le temps necessaire pour déverser volumeExtinction 
	 */
	public double getTempsExtinction() {
		return this.tempsExtinction;
	}

	public double getVolumeEauRestant(){
		return (double) this.reservoir * this.remplissage;
	}

	/*
	 * Permet de mettre à jour le niveau de remplissage après déversement d'eau 
	 */
	public void deverserEau(double volume){
		this.setRemplissage(remplissage - volume/reservoir);
	}

	/*
	 * Calcule le temps nécessaire au robot pour verser 
	 * le volume d'eau entré en paramètre 
	 */
	public double tempsDeversementEau(double volume){
		if (volume <= this.getVolumeEauRestant()){
			return volume/volumeExtinction * tempsExtinction;
		} else{
			return this.getVolumeEauRestant()/volumeExtinction * tempsExtinction;
		}
	}

	/*
	 * Permet d'afficher le robot sur l'interface graphique 
	 */
	public void afficherRobot(Affichage image){
		image.dessineRobot(this.position.getColonne(), this.position.getLigne(), this.type, true);
	}

	/*
	 * Remet le robot à son état initiale 
	 */
	public void reset(){
		this.resetPosition();
		remplissage = 1;
	}

	@Override
	public String toString(){
		String s = "Robot de type";
		switch(type){
			case DRONE:
			s = s + " DRONE ";
			case ROBOTACHENILLE:
			s = s + " ROBOT À CHENILLES ";
			case ROBOTAPATTES:
			s = s + " ROBOT À PATTES";
			case ROBOTAROUE:
			s = s + " ROBOT À ROUES";
		}
		s = s + "\nCoordonnées : " + position;
		s = s + "\nParamètres :";
		s = s + "\nVITESSE : " + vitesse + "\nreservoir MAXIMALE : " + reservoir;
		s = s + "\nREMPLISSAGE : " + remplissage + "% --> " + remplissage*reservoir + "L";
		if (estOccupe){
			s = s + "\nETAT COURANT : OCCUPÉ\n";
		} else {
			s = s + "\nETAT COURANT : LIBRE\n";
		}
		return s;
	}
}
