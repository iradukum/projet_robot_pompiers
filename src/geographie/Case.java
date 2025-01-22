package geographie;

/*******************************************************************************
* Classe Case définie par deux coordonnées entières (ligne et colonne), 
* la carte à laquelle la case appartient et la nature du terrain qu’elle représente
********************************************************************************/

public class Case{
	private int ligne;
	private int colonne;
	private Carte reference;
	private NatureTerrain nature;
	
	public enum NatureTerrain{EAU, FORET, ROCHE, TERRAIN_LIBRE, HABITAT}

	public Case(int ligne, int colonne) {
		this.ligne = ligne;
		this.colonne = colonne;
	}

	public Case(int ligne, int colonne, Carte reference){
		this.ligne = ligne;
		this.colonne = colonne;
		this.reference = reference;
	}

	public int getLigne() {
		return this.ligne;
	}

	public int getColonne() {
		return this.colonne;
	}

	public NatureTerrain getNature() {
		return this.nature;
	};
	
	public void setNature(NatureTerrain nature) {
		this.nature = nature;
	}
	
	public Carte getCarteReference(){
		return this.reference;
	}
	
	@Override
	public String toString(){
		String s = "Case de coordonnées (" + ligne + " , " + colonne + ")";
		return s;
	}

}
