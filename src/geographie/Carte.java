package geographie;

import affichage.Affichage;


/***************************************************************************
* Classe Carte  contient une matrice de Case, et la taille du côté des cases. 
* Cette classe fournit notamment des méthodes pour accéder à une case, ou pour 
* trouver son voisin dans une direction donnée.
****************************************************************************/

public class Carte {
	int nbLignes;
	int nbColonnes;
	int tailleCases;
	Case[][] Table;
	
	public enum Direction{NORD, SUD, EST, OUEST}
	
	public Carte(int nbLignes, int nbColonnes, int tailleCases) {
		this.nbLignes = nbLignes;
		this.nbColonnes = nbColonnes;
		this.tailleCases = tailleCases;
		this.Table = new Case[nbLignes][nbColonnes];
		for (int i=0; i<this.nbLignes; i++) {
			for (int j=0; j<this.nbColonnes; j++) {
				this.Table[i][j] = new Case(i,j,this);
			}
		}
	}

	public int getNbLignes() {
		return nbLignes;
	}

	public int getNbColonnes() {
		return nbColonnes;
	}

	public int getTailleCases() {
		return tailleCases;
	}
	
	public Case getCase(int lig, int col) {
	    return this.Table[lig][col];
	}

	public void setNatureCase(int lig, int col, Case.NatureTerrain nature) {
	    this.Table[lig][col].setNature(nature);
	}
	
	public boolean voisinExiste(Case src, Direction dir) {
        switch(dir){
			case NORD:
				if(src.getLigne()==0){
					return false;
				}
				return true;
			case SUD:
				if(src.getLigne()==this.nbLignes-1){
					return false;
				}
				return true;
			case OUEST:
				if(src.getColonne()==0){
					return false;
				}
				return true;
			case EST:
				if(src.getColonne()==this.nbColonnes-1){
					return false;
				}
				return true;
		}
        return false;
	}

	public Case getVoisin(Case src, Direction dir){
		if (this.voisinExiste(src, dir)){
			int i = src.getLigne();
			int j = src.getColonne();
			if (dir == Direction.NORD) {
				return getCase(i-1, j);
			}
			if (dir == Direction.SUD) {
				return getCase(i+1, j);
			}
			if (dir == Direction.EST) {
				return getCase(i, j+1);
			}
			else {
				return getCase(i, j-1);
			}
		}
		return null;
	}
	
	public void afficherCarte(Affichage image){
		for (int i = 0; i<this.nbLignes; i++){
			for (int j=0; j<this.nbColonnes; j++){
				image.dessineTerrain(j, i, this.Table[i][j].getNature(), true);
			}
		}
	}

	@Override
	public String toString(){
		String s;
		s = "Nombre de lignes : " + this.nbLignes + "\n";
		s += "Nombre de colonnes : " + this.nbColonnes + "\n";
		s += "Taille des cases : " + this.tailleCases + "\n";
		return s;
	}
}
